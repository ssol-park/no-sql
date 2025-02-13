package com.psr.nosql.controller;

import com.psr.nosql.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class VideoController {

    /**
     * 특정 영상의 조회수를 증가시키고, 해당 영상의 URL을 반환한다.
     *
     * @param videoId 조회수를 증가시킬 영상의 ID
     * @return 조회수와 영상 URL을 포함한 응답 데이터
     */
    @PostMapping("/{videoId}")
    public ResponseEntity<ResponseDto> incrementViewsAndGetUrl(@PathVariable String videoId) {
        return null;
    }

    /**
     * 오늘의 인기 영상 상위 5개를 조회하여 반환한다.
     *
     * @return 인기 영상 목록
     */
    @GetMapping("/popular/today")
    public ResponseEntity<ResponseDto> getPopularVideos() {
        return null;
    }
}
