package com.ticketaka.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String,String> redisTemplate;
    public void setValues(String token,Long memberId){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(token,memberId.toString());
    }
    public void deleteValue(String refreshToken){
        redisTemplate.delete(refreshToken);
    }
    public Long getMemberId(String refreshToken){ // refresh 토큰을 이용해 memberId 를 반환
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return Long.valueOf(valueOperations.get(refreshToken));

    }
}
