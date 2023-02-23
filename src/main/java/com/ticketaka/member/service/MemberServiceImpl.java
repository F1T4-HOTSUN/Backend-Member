package com.ticketaka.member.service;

import com.ticketaka.member.dto.RsvMemberDTO;
import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDto;
import com.ticketaka.member.dto.request.SignupRequestDto;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.feign.ReservationFeignClient;
import com.ticketaka.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final ReservationFeignClient reservationFeignClient;
    @Override
    @Transactional
    public StatusCode signUp(SignupRequestDto dto) throws SQLException{
        // entity 로 변경 후 save
//        try{
            Member member = memberRepository.save(dto.toEntity());
            String email = member.getEmail();
            Long id = member.getId();

//            reservationFeignClient.createMember(RsvMemberDTO
//                    .builder()
//                    .memberEmail(email)
//                    .memberId(id)
//                    .build());
            return StatusCode.OK;
//        }catch(DataIntegrityViolationException e){
//            log.error(e.toString());
//            log.info("이메일 중복 ");
//            return StatusCode.DUPLICATE_MEMBER;
//        }
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(NoSuchElementException::new);
        if (!member.getPassword().equals(dto.getPassword())) {
            throw new NoSuchElementException();
        }
        return LoginResponseDto.builder().memberId(member.getId()).role(member.getRole()).build();
    }

    @Override
    @Transactional(readOnly = true)
    public StatusCode checkDuplicateMember(String email) {
        // Optional 람다로 바꿔봅시다
        log.info("email : {} ", email);
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isEmpty()){
            return StatusCode.OK;
        }
        return StatusCode.DUPLICATE_EMAIL;
    }

    @Override
    @Transactional(readOnly = true)
    public InfoResponseDto getInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);// 없을 때 예외를 떤짐
        log.info("member Info {}", member.toString());
        // memberId 로 InfoResponseDto 을 만들어 반환
        return member.toInfoResponseDto();
    }
}
