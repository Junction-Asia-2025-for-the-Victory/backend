package com.victory.evertalk.domain.episode.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class SttService {

    private final WebClient openAiWebClient;

    @Value("${OPENAI_API_KEY}")
    private String openAiApiKey;

    // 필요시 조절 (OpenAI는 보통 25MB 권장, 최신 플랜/모델에 따라 달라질 수 있음)
    private static final long MAX_FILE_SIZE_BYTES = 25L * 1024 * 1024;

    /**
     * .webm 음성 파일을 STT로 변환하여 텍스트를 반환
     */
    public String transcribeWebm(MultipartFile audioFile) {
        validateFile(audioFile);

        // ByteArrayResource로 파일명 제공(멀티파트 전송 시 filename 필수)
        ByteArrayResource fileResource = new ByteArrayResource(toBytes(audioFile)) {
            @Override
            public String getFilename() {
                // 원본 파일명이 없을 수도 있으니 기본값 처리
                String original = audioFile.getOriginalFilename();
                return (original != null && !original.isBlank()) ? original : "audio.webm";
            }
        };

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", fileResource)
                .filename(fileResource.getFilename())
                .contentType(MediaType.parseMediaType(audioFile.getContentType() != null
                        ? audioFile.getContentType()
                        : "audio/webm"));
        bodyBuilder.part("model", "whisper-1"); // 또는 "gpt-4o-mini-transcribe" 등 최신 STT 모델로 교체 가능
        // 선택 파라미터
         bodyBuilder.part("language", "en"); // 언어 고정 시
        // bodyBuilder.part("prompt", "domain hints..."); // 인식 힌트
        // bodyBuilder.part("temperature", "0"); // 안정적 출력을 원할 때
        // bodyBuilder.part("response_format", "json"); // 기본 json

        try {
            TranscriptionResponse response = openAiWebClient.post()
                    .uri("https://api.openai.com/v1/audio/transcriptions")
                    .headers(h -> h.setBearerAuth(openAiApiKey))
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .bodyValue(bodyBuilder.build())
                    .retrieve()
                    .bodyToMono(TranscriptionResponse.class)
                    .onErrorResume(WebClientResponseException.class, ex -> {
                        log.error("OpenAI STT API error: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString(StandardCharsets.UTF_8));
                        return Mono.error(ex);
                    })
                    .block();

            if (response == null || response.text == null) {
                throw new IllegalStateException("Empty transcription response from OpenAI");
            }
            return response.text.trim();

        } catch (WebClientResponseException e) {
            throw new RuntimeException("[STT] OpenAI API 호출 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("[STT] 변환 처리 중 오류가 발생했습니다.", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 음성 파일이 비어 있습니다.");
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new IllegalArgumentException("파일 용량이 너무 큽니다. 최대 " + (MAX_FILE_SIZE_BYTES / (1024 * 1024)) + "MB 까지 허용됩니다.");
        }
        String ct = file.getContentType();
        // 웹브라우저/클라이언트에 따라 "audio/webm" 또는 "video/webm"로 올 수 있음
        if (ct == null || !(ct.equalsIgnoreCase("audio/webm") || ct.equalsIgnoreCase("video/webm"))) {
            log.warn("Non-webm content-type received: {}", ct);
            // content-type 판별이 애매하면 확장자로 재검증
            String name = file.getOriginalFilename() != null ? file.getOriginalFilename().toLowerCase() : "";
            if (!name.endsWith(".webm")) {
                throw new IllegalArgumentException("지원하지 않는 형식입니다. .webm 파일만 업로드 해주세요.");
            }
        }
    }

    private byte[] toBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("업로드 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    // OpenAI /v1/audio/transcriptions 기본 응답 형태: { "text": "..." }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TranscriptionResponse(String text) {}
}
