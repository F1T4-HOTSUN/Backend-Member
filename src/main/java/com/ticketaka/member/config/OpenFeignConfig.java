package com.ticketaka.member.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.ticketaka.member")
public class OpenFeignConfig {
}
