local key = KEYS[1]
local now = tonumber(ARGV[1])
local window = tonumber(ARGV[2]) * 1000
local limit = tonumber(ARGV[3])
local ttl = tonumber(ARGV[4])

redis.call('ZREMRANGEBYSCORE', key, 0, now - window)

-- 2. 현재 요청 개수 확인
local count = redis.call('ZCARD', key)

-- 3. 허용 개수를 초과하면 요청 차단
if count >= limit then
    return 0
end

-- 4. 새로운 요청 추가
redis.call('ZADD', key, now, now)

-- 5. TTL 설정 (최적화)
redis.call('EXPIRE', key, ttl)

return 1
