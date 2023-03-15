package com.ticketaka.member.feign;

import com.ticketaka.member.dto.request.ReservationDTO;
import com.ticketaka.member.dto.response.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="ReservationFeignClient", url="${reservation.url}"+":${reservation.port}" ,path="/reservation")
public interface ReservationFeignClient {


    @PostMapping
    BaseResponse reservation(@RequestHeader("memberid") Long memberId, @RequestBody ReservationDTO dto);

    @GetMapping("/lists/{member_id}")
    BaseResponse reservationList(@PathVariable("member_id") Long memberId);

    @GetMapping("/list/{rsv_id}")
    BaseResponse reservationInfo(@PathVariable("rsv_id") Long reservationId);

    @DeleteMapping("/delete/{rsv_id}")
    BaseResponse deleteReservation(@PathVariable("rsv_id") Long reservationId);
}
