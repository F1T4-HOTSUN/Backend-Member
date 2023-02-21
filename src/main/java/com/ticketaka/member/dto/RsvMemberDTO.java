package com.ticketaka.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RsvMemberDTO {
    private Long memberId;
    private String memberEmail;
}
