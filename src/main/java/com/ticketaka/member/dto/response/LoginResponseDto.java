package com.ticketaka.member.dto.response;

import com.ticketaka.member.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class LoginResponseDto {

    private Long memberId;
    private Role role;
}
