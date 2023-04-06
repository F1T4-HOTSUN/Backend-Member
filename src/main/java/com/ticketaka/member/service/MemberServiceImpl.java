package com.ticketaka.member.service;

import com.ticketaka.member.dto.StatusCode;
import com.ticketaka.member.dto.request.LoginRequestDTO;
import com.ticketaka.member.dto.request.SignupRequestDTO;
import com.ticketaka.member.dto.request.ValidateNumberDTO;
import com.ticketaka.member.dto.response.InfoResponseDto;
import com.ticketaka.member.dto.response.LoginResponseDto;
import com.ticketaka.member.entity.Member;
import com.ticketaka.member.repository.MemberRepository;
import com.ticketaka.member.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    private final RabbitTemplate rabbitTemplate;

    private final RedisService redisService;

    @Override
    @Transactional
    public StatusCode signUp(SignupRequestDTO dto){
        // entity 로 변경 후 save
            Member member = memberRepository.save(dto.toEntity());
            String email = member.getEmail();
            Long id = member.getId();

            return StatusCode.OK;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDTO dto) {
        Member member = memberRepository.findByEmail(dto.getEmail()).orElseThrow(NoSuchElementException::new);
        if (!member.getPassword().equals(dto.getPassword())) {
            throw new NoSuchElementException();
        }
        member.getId();
        String accessToken = jwtUtils.generateAccessToken(member);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",accessToken);
        return LoginResponseDto.builder().memberId(member.getId()).role(member.getRole()).headers(headers).build();
    }

    @Override
    @Transactional(readOnly = true)
    public StatusCode checkDuplicateMember(String email) {
        // Optional 람다로 바꿔봅시다
        log.info("email : {} ", email);
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(!byEmail.isEmpty()){
            return StatusCode.DUPLICATE_EMAIL;
        }
        // 중복체크가 통과하면 Redis 저장, 메일로 인증번호 보내기
        String num = makeRandomNum(6);
        redisService.setValues(email, num);

        ValidateNumberDTO dto = ValidateNumberDTO.builder()
                                .email(email)
                                .authNum(num)
                                .build();
        rabbitTemplate.convertAndSend("valid.exchange", "valid.key", dto);
        return StatusCode.OK;
    }

    @Override
    @Transactional(readOnly = true)
    public InfoResponseDto getInfo(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);// 없을 때 예외를 떤짐
        log.info("member Info {}", member.toString());
        // memberId 로 InfoResponseDto 을 만들어 반환
        return member.toInfoResponseDto();
    }

    @Override
    public StatusCode validateNumber(ValidateNumberDTO dto) {
        // email 가져오고 -> Redis  에서 조회한 뒤 해당 번호가 일치하는지 중복성 체크
        String authNum = redisService.getAuthNum(dto.getEmail());
        // 일치하지 않으면 에러코드 던짐
        if (!authNum.equals(dto.getAuthNum())) {
            return StatusCode.INVALID_NUM;
        }
        // 일치하면 Redis 에서 삭제 후 상태코드 반환
        redisService.deleteValue(dto.getEmail());
        return StatusCode.OK;
    }

    private String makeRandomNum(int n){
        // 길이가 n 인 랜덤 문자열을 생성하는 메서드
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i=0;i<n;i++){
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
