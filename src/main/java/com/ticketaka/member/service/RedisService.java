package com.ticketaka.member.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

public interface RedisService {
    void setValues(String email,String authNum);
    void deleteValue(String email);
    String getAuthNum(String email);
}
