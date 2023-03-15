package com.ticketaka.member.service;

import com.ticketaka.member.dto.request.ReservationDTO;
import com.ticketaka.member.dto.response.BaseResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

public interface ReservationService {
    BaseResponse reservation(Long memberId, ReservationDTO dto);
    //BaseResponse reservationList(@RequestHeader Map<String, String> header);

    //BaseResponse reservationInfo(@PathVariable("rsv_id") Long reservationId);

    //BaseResponse deleteReservation(@PathVariable("rsv_id") Long reservationId);
}
