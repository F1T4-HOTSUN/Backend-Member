package com.ticketaka.member.service;


import com.ticketaka.member.dto.request.ReservationDTO;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.feign.ReservationFeignClient;
import com.ticketaka.member.repository.MemberRepository;
import com.ticketaka.member.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService{


    private final MemberRepository memberRepository;
    private final ReservationFeignClient reservationFeignClient;

    @Override
    public BaseResponse reservation(Long memberId, ReservationDTO dto) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchFieldError::new);
        dto.setMemberEmail(member.getEmail());
        return reservationFeignClient.reservation(memberId, dto);
    }
}
