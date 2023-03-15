package com.ticketaka.member.util;


import com.ticketaka.member.dto.response.BaseResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtils {
    public ResponseEntity<BaseResponse> makeResponse(BaseResponse baseResponse){
        return ResponseEntity.status(baseResponse.getCode()).body(baseResponse);
    }
    public ResponseEntity<BaseResponse> makeResponse(BaseResponse baseResponse, HttpHeaders headers){
        return ResponseEntity.status(baseResponse.getCode()).headers(headers).body(baseResponse);
    }
}
