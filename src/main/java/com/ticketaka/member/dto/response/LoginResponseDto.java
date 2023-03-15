package com.ticketaka.member.dto.response;

import com.ticketaka.member.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@Getter
@Builder
public class LoginResponseDto {
    private HttpHeaders headers;
    private Long memberId;
    private Role role;
}
