package com.phishing.authservice.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    public void setRedisValues (String key, String value, Duration time) {
        if (getRedisValues(key) != null){
            deleteRedisValues(key);
        }
        redisTemplate.opsForValue().set(key, value, time);
    }

    public String getRedisValues (String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteRedisValues (String key) {
        redisTemplate.delete(key);
    }

    public boolean isExistKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
