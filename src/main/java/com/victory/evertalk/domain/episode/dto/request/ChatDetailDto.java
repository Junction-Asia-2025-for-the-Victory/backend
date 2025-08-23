package com.victory.evertalk.domain.episode.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDetailDto {

    // "character" | "user"
    @JsonProperty("speaker")
    private Speaker speaker;

    @NotBlank
    @JsonProperty("text")
    private String text;

}

enum Speaker {
    CHARACTER("character"),
    USER("user");

    private final String json;

    Speaker(String json) { this.json = json; }

    // 직렬화 시 소문자 문자열로 출력
    @JsonValue
    public String toJson() { return json; }

    // 역직렬화 시 대소문자 구분 없이 매핑
    @JsonCreator
    public static Speaker from(String v) {
        if (v == null) return null;
        String s = v.trim().toLowerCase();
        return switch (s) {
            case "character" -> CHARACTER;
            case "user" -> USER;
            default -> throw new IllegalArgumentException("Unknown speaker: " + v);
        };
    }
}