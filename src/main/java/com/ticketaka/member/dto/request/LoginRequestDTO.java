package com.ticketaka.member.dto.request;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequestDTO {

    private String email;
    private String password;

}
