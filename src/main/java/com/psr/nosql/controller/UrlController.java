package com.psr.nosql.controller;

import com.psr.nosql.dto.ApiResponse;
import com.psr.nosql.dto.ShortUrlResponse;
import com.psr.nosql.dto.UrlRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/url")
@Tag(name = "URL 단축 API", description = "단축 URL 생성 및 조회 관련 API")
public class UrlController {

    /**
     * 단축 URL을 생성
     *
     * @param request 원본 URL과 TTL 정보를 담은 요청 객체
     * @return 생성된 단축 URL 정보 (shortCode, originalUrl, TTL 등)
     */
    @Operation(summary = "단축 URL 생성", description = "원본 URL을 단축 코드로 변환한다.")
    @PostMapping("/shorten")
    public ApiResponse<ShortUrlResponse> shortenUrl(@RequestBody UrlRequestDto request) {
        // TODO: 서비스 로직 호출
        ShortUrlResponse response = null;
        return ApiResponse.success(response);
    }

    /**
     * 단축 URL을 통해 원본 URL로 리디렉션 정보를 반환
     *
     * @param shortCode 단축 URL 코드
     * @return 단축 코드에 해당하는 원본 URL 정보
     */
    @GetMapping("/{shortCode}")
    @Operation(summary = "단축 URL 조회", description = "단축 코드를 통해 원본 URL 정보를 반환한다.")
    public ApiResponse<ShortUrlResponse> redirect(@PathVariable String shortCode) {
        // TODO: 서비스 로직 호출
        ShortUrlResponse response = null;
        return ApiResponse.success(response);
    }

    /**
     * 인기 URL 목록을 조회
     *
     * @return 조회수가 높은 단축 URL 리스트
     */
    @GetMapping("/popular")
    @Operation(summary = "인기 URL 조회", description = "조회수가 높은 단축 URL 목록을 반환한다.")
    public ApiResponse<List<ShortUrlResponse>> getPopularUrls() {
        // TODO: 서비스 로직 호출
        List<ShortUrlResponse> popularUrls = Collections.emptyList();
        return ApiResponse.success(popularUrls);
    }
}
