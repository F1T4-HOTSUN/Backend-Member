package com.ticketaka.member;

import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void 회원가입_테스트(){
        SignupRequestDto dto = SignupRequestDto.builder()
                        .email("rhj00830@naver.com")
                        .password("pass123#")
                        .name("노희재")
                        .birth("1997-08-30")
                        .phone("010-2929-7675")
                        .gender("남")
                        .build();

        ResponseEntity<String> response = memberService.signUp(dto);
        Assertions.assertThat(response.getBody().equals(""));
    }
    @Test
    public void 로그인_테스트(){
        memberService.login();
    }

    @Test
    public void 회원중복_테스트(){
        memberService.checkDuplicateMember();
    }

    @Test
    public void 멤버상세정보_테스트(){
        memberService.getInfo(1L);
    }

}
