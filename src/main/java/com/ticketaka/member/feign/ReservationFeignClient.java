package com.ticketaka.member.feign;

import com.ticketaka.member.dto.RsvMemberDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="ReservationFeignClient", url="${reservation.url}"+":${reservation.port}" ,path="/reservation")
public interface ReservationFeignClient {
    @GetMapping("/create/member")
    ResponseEntity<String> createMember(@RequestBody RsvMemberDTO rsvMemberDTO);
}
