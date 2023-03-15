package com.ticketaka.member.controller;


import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.ReservationDTO;
import com.ticketaka.member.dto.response.BaseResponse;
import com.ticketaka.member.service.ReservationService;
import com.ticketaka.member.util.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final ResponseUtils responseUtils;
    // ??

    @PostMapping("/create")
    public BaseResponse reservation(@RequestHeader("memberid") Long memberId, @RequestBody ReservationDTO dto) {
        reservationService.reservation(memberId, dto);
        return new BaseResponse(StatusCode.OK);
    }


//    @GetMapping("/lists")
//    public ResponseEntity<BaseResponse> reservationList(@RequestHeader Map<String, String> header) {
//        return responseUtils.makeResponse(reservationService.reservationList(header));
//    }

//    @GetMapping("/list/{rsv_id}")
//    public ResponseEntity<BaseResponse> reservationInfo(@PathVariable("rsv_id") Long reservationId) {
//        return responseUtils.makeResponse(reservationService.reservationInfo(reservationId));
//    }
//
//    @DeleteMapping("/delete/{rsv_id}")
//    public ResponseEntity<BaseResponse> deleteReservation(@PathVariable("rsv_id") Long reservationId) {
//        return responseUtils.makeResponse(reservationService.deleteReservation(reservationId));
//    }
}