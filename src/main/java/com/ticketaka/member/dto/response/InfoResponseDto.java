package com.ticketaka.member.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class InfoResponseDto {
    private String name;
    private String email;
    private String phone;
    private LocalDate birth;
}
