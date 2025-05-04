package com.psr.nosql.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "단축 URL 응답 DTO")
public class ShortUrlResponse {

    @NotBlank
    @Schema(description = "생성된 단축 코드", example = "aB12Cd")
    private String shortCode;

    @NotBlank
    @Schema(description = "원본 URL", example = "https://example.com")
    private String originalUrl;

    @NotBlank
    @Schema(description = "단축 URL이 생성된 시각", example = "2025-05-04T13:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "만료 예정 시각", example = "2025-05-05T13:00:00")
    private LocalDateTime expiresAt;
}
