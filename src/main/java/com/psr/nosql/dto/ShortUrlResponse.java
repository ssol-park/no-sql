package com.psr.nosql.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "단축 URL 응답 DTO")
public class ShortUrlResponse {

    @Schema(description = "접속 가능한 단축 URL", example = "https://short.ly/aB12Cd")
    private String shortUrl;
}
