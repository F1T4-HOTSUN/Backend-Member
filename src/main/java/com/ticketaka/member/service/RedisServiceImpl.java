package com.ticketaka.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService{
    private final RedisTemplate<String,String> redisTemplate;

    public void setValues(String email,String authNum){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(email,authNum);
        redisTemplate.expire(email, 4, TimeUnit.MINUTES);
    }
    public void deleteValue(String email){
        redisTemplate.delete(email);
    }
    public String getAuthNum(String email){ // refresh 토큰을 이용해 memberId 를 반환
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(email);
    }
}
