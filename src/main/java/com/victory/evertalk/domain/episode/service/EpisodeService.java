package com.victory.evertalk.domain.episode.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.victory.evertalk.domain.character.entity.Character;
import com.victory.evertalk.domain.character.entity.Emotion;
import com.victory.evertalk.domain.character.entity.Face;
import com.victory.evertalk.domain.character.entity.Likeability;
import com.victory.evertalk.domain.character.repository.LikeabilityRepository;
import com.victory.evertalk.domain.character.service.CharacterReadService;
import com.victory.evertalk.domain.character.service.EmotionReadService;
import com.victory.evertalk.domain.character.service.FaceReadService;
import com.victory.evertalk.domain.character.service.LikeabilityReadService;
import com.victory.evertalk.domain.episode.dto.request.ChatDetailDto;
import com.victory.evertalk.domain.episode.dto.request.UserAnswerFeedbackRequestDto;
import com.victory.evertalk.domain.episode.dto.response.AIUserAnswerFeedbackResponseDto;
import com.victory.evertalk.domain.episode.dto.response.EpisodeListDetailResponseDto;
import com.victory.evertalk.domain.episode.dto.response.EpisodeListResponseDto;
import com.victory.evertalk.domain.episode.dto.response.StartEpisodeResponseDto;
import com.victory.evertalk.domain.episode.entity.Chat;
import com.victory.evertalk.domain.episode.entity.Episode;
import com.victory.evertalk.domain.episode.repository.ChatRepository;
import com.victory.evertalk.domain.user.entity.User;
import com.victory.evertalk.domain.user.service.UserReadService;
import com.victory.evertalk.global.common.client.FastApiClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EpisodeService {

    private final EpisodeReadService episodeReadService;
    private final UserReadService userReadService;
    private final LikeabilityReadService likeabilityReadService;
    private final ProgressReadService progressReadService;
    private final FaceReadService faceReadService;
    private final CharacterReadService characterReadService;
    private final ChatRepository chatRepository;
    private final ChatReadService chatReadService;
    private final FastApiClientService fastApiClientService;
    private final EmotionReadService emotionReadService;
    private final LikeabilityRepository likeabilityRepository;

    private final ObjectMapper om;

    private Integer characterId = 1;

    public EpisodeListResponseDto searchEpisodeList(Integer userId){

        userReadService.findUserByIdOrElseThrow(userId);

        Integer likeability = likeabilityReadService.findLikeabilityByUserIdAndCharacterId(userId, characterId).getCount();
        Integer progress = progressReadService.findProgressByUserIdAndCharacterId(userId).getEpisodeNum();

        List<Episode> episodeList = episodeReadService.findEpisodeListByUserIdAndCharacterId(userId);

        List<EpisodeListDetailResponseDto> episodeDetailList = new ArrayList<>();
        for(Episode episode:episodeList){
            EpisodeListDetailResponseDto episodeDetail = EpisodeListDetailResponseDto.builder()
                    .episodeId(episode.getEpisodeId())
                    .episodeTitle(episode.getTitle())
                    .build();
            episodeDetailList.add(episodeDetail);
        }

        return EpisodeListResponseDto.builder()
                .likeability(likeability)
                .progress(progress)
                .episodeDetail(episodeDetailList)
                .build();
    }

        public StartEpisodeResponseDto StartEpisode(Integer userId, Integer episodeId) {
            User user = userReadService.findUserByIdOrElseThrow(userId);

            Episode episode = episodeReadService.findEpisodeByEpisodeId(episodeId);
            Emotion emotion = emotionReadService.findEmotionByEmotion("normal");

            Face face = faceReadService.findFaceByCharacter_CharacterIdAndEmotion_EmotionId(characterId, emotion.getEmotionId());
            Character character = characterReadService.findCharacterByCharacterId(characterId);

            Likeability likeability = likeabilityReadService.findLikeabilityByUserIdAndCharacterId(userId, characterId);

            String chatJson = buildInitialChatJson(episode.getStart());

            Chat chat = Chat.builder()
                    .episode(episode)
                    .user(user)
                    .count(0)
                    .finish(false)
                    .character(character)
                    .chat(chatJson)
                    .build();

        Chat saved = chatRepository.save(chat);       // 2) save → PK 생성

        return StartEpisodeResponseDto.builder()
                .chatId(saved.getChatId())
                .chat(episode.getStart())
                .img(face.getUrl())
                .changeLike(0)
                .characterName(character.getCharacterName())
                .feedback("")
                .lastChat(false)
                .nickname(user.getNickname())
                .likeability(likeability.getCount())
                .build();
    }

//    public StartEpisodeResponseDto userAnswer(Integer userId, Integer chatId, String text){
    public StartEpisodeResponseDto userAnswer(Integer userId, Integer chatId, String text){
        User user = userReadService.findUserByIdOrElseThrow(userId);
        Chat chat = chatReadService.findChatByChatId(chatId);
        Episode episode = episodeReadService.findEpisodeByEpisodeId(chat.getEpisode().getEpisodeId());
        Character character = characterReadService.findCharacterByCharacterId(chat.getCharacter().getCharacterId());
        Likeability likeability = likeabilityReadService.findLikeabilityByUserIdAndCharacterId(userId, characterId);

        List<ChatDetailDto> previous = toPreviousChat(chat.getChat());

        // 이번 유저 발화가 반영되면 사용자 턴 수가 몇 번째가 되는지 미리 계산
        int userTurnsAfter = chat.getCount() + 1;
        boolean lastChat = userTurnsAfter >= 10;
        
        // fast API 호출하기 위한 DTO 생성
        UserAnswerFeedbackRequestDto fastApiRequestDto = UserAnswerFeedbackRequestDto.builder()
                .character_name(character.getCharacterName())
                .character_info(character.getPersonality())
                .background_situation(episode.getBackground())
                .previous_chat(previous)
                .affinity(likeability.getCount())
                .last_user_input(text)
                .user_gender(user.getGender())
                .user_nickname(user.getNickname())
                .build();

        AIUserAnswerFeedbackResponseDto fastApiResponseDto = fastApiClientService.sendUserAnswerToFastApi(fastApiRequestDto);

        Emotion emotion = emotionReadService.findEmotionByEmotion(fastApiResponseDto.getEmotion());

        appendLine(chatId, "USER", text);
        Face face = faceReadService.findFaceByCharacter_CharacterIdAndEmotion_EmotionId(characterId, emotion.getEmotionId());

        likeability.updateLikeability(fastApiResponseDto.getAffinity());
        likeabilityRepository.save(likeability);


        return StartEpisodeResponseDto.builder()
                .chatId(chatId)
                .chat(fastApiResponseDto.getNext_utterance())
                .img(face.getUrl())
                .likeability(likeability.getCount())
                .changeLike(fastApiResponseDto.getAffinity() - likeability.getCount())
                .feedback(fastApiResponseDto.getLast_user_correction_input())
                .lastChat(lastChat)
                .nickname(user.getNickname())
                .characterName(character.getCharacterName())
                .build();

    }

    private List<ChatDetailDto> toPreviousChat(String chatJson) {
        if (chatJson == null || chatJson.isBlank()) return List.of();

        try {
            JsonNode root = om.readTree(chatJson);
            if (!root.isArray()) return List.of();

            // 이제 root가 바로 배열이므로 간단함
            ArrayNode normalized = om.createArrayNode();
            for (JsonNode n : root) {
                String rawSpeaker = textOf(n, "speaker", null);
                String text = textOf(n, "text", "");
                if (rawSpeaker == null) continue;

                String normalizedSpeaker = switch (rawSpeaker.trim().toUpperCase()) {
                    case "AI", "CHARACTER", "ASSISTANT", "BOT" -> "character";
                    case "USER", "HUMAN" -> "user";
                    default -> throw new IllegalArgumentException("Unknown speaker: " + rawSpeaker);
                };

                ObjectNode one = om.createObjectNode();
                one.put("speaker", normalizedSpeaker);
                one.put("text", text);
                normalized.add(one);
            }

            return om.convertValue(normalized, new TypeReference<List<ChatDetailDto>>() {});

        } catch (Exception e) {
            log.error("Failed to parse/normalize previous_chat: {}", chatJson, e);
            throw new RuntimeException("Failed to parse/normalize previous_chat", e);
        }
    }

    private String textOf(JsonNode node, String key, String defVal) {
        JsonNode v = node.get(key);
        return (v != null && v.isTextual()) ? v.asText() : defVal;
    }

    // 시작 JSON 생성 (키 통일: speaker/text)
    private String buildInitialChatJson(String startLine) {
        try {
            ObjectNode root = om.createObjectNode();
            ArrayNode messages = om.createArrayNode();

            ObjectNode first = om.createObjectNode();
            first.put("speaker", "AI");
            first.put("text", startLine != null ? startLine : "");
            messages.add(first);

            root.set("messages", messages);
            return om.writeValueAsString(root);
        } catch (Exception e) {
            // 폴백도 동일한 키로
            return "{\"messages\":[{\"speaker\":\"AI\",\"text\":\"" + escapeJson(startLine) + "\"}]}";
        }
    }

    // 유저 발화/AI응답 누적
    @Transactional
    public void appendLine(Integer chatId, String speaker, String text) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        try {
            // 기존 문자열(JSON 배열) → 배열 노드
            String current = chat.getChat();
            ArrayNode messages;

            if (current == null || current.isBlank()) {
                // 첫 번째 메시지인 경우 빈 배열 생성
                messages = om.createArrayNode();
            } else {
                // 기존 배열 파싱
                JsonNode root = om.readTree(current);
                messages = root.isArray() ? (ArrayNode) root : om.createArrayNode();
            }

            // 새 메시지 추가
            ObjectNode m = om.createObjectNode();
            m.put("speaker", speaker);  // "AI" or "USER"
            m.put("text", text);
            messages.add(m);

            // 배열 → 문자열 저장
            chat.addChat(messages.toString());

            // 사용자 턴만 카운트
            if ("USER".equals(speaker)) {
                chat.addCount(chat.getCount() + 1);
            }

        } catch (Exception e) {
            throw new RuntimeException("Append chat failed (invalid JSON)", e);
        }
    }

    private String escapeJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
