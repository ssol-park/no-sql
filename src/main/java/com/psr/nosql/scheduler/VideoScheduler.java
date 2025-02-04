package com.psr.nosql.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VideoScheduler {

    /**
     * 매일 자정에 조회수 데이터를 기반으로 인기 영상을 갱신
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void updatePopularVideos() {
    }

    /**
     * 1시간마다 실행하여 인기 영상 목록에서 오래된 영상을 정리한다.
     * 인기 영상 상위 5개만 유지하고, 나머지 영상은 제거
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void removeOldVideos() {
    }
}
