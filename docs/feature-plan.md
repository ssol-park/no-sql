# docs/feature-plan.md

## 방문 로그 기록 (MongoDB)
- 트래픽이 많으므로 비동기로 처리
- 필드: shortCode, timestamp, ip, userAgent
- 컬렉션 이름: visit_logs
- TTL 적용 고려 (30일 후 자동 만료)

## Redis 캐시 처리
- 조회 시 Redis 우선 → 없으면 RDB → Redis 저장
- key: url:{shortCode} → originalUrl (TTL 1일)
- 인기 URL: Sorted Set (key: url:popular), ZINCRBY로 조회수 증가
- TTL로 자동 만료, 정기적으로 리셋 가능

## 향후 고려사항
- TTL 만료 처리 → 스케줄러 혹은 Redis TTL 활용
- 비동기 처리 → ExecutorService, Kafka 또는 Spring Event 가능성 검토
- 데이터 정합성 검증 로직 추가 예정
