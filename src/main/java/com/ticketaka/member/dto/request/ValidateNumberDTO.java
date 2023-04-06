package com.ticketaka.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class ValidateNumberDTO {
    private String email;
    private String authNum;
}
