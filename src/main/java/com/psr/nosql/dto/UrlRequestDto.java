package com.psr.nosql.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "단축 URL 생성 요청 DTO")
public class UrlRequestDto {

    @NotBlank
    @Schema(description = "단축하고자 하는 원본 URL", example = "https://example.com")
    private String originalUrl;
}
