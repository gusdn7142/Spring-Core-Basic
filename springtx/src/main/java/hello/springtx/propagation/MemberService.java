package hello.springtx.propagation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LogRepository logRepository;

    //1. 회원 등록 & 로그 등록
    public void joinV1(String username) {
        Member member = new Member(username);  //Member 객체 생성
        Log logMessage = new Log(username);    //(username필드값을 message에 넣어) Log 객체 생성

        log.info("== memberRepository 호출 시작 ==");
        memberRepository.save(member);         //회원 등록
        log.info("== memberRepository 호출 종료 ==");

        log.info("== logRepository 호출 시작 ==");
        logRepository.save(logMessage);        //로그 등록
        log.info("== logRepository 호출 종료 ==");
    }



    //2. 회원 등록 & 로그 등록(+예외처리)
    public void joinV2(String username) {

        Member member = new Member(username);  //Member 객체 생성
        Log logMessage = new Log(username);    //(username필드값을 message에 넣어) Log 객체 생성

        log.info("== memberRepository 호출 시작 ==");
        memberRepository.save(member);        //회원 등록
        log.info("== memberRepository 호출 종료 ==");


        log.info("== logRepository 호출 시작 ==");
        try {
            logRepository.save(logMessage);   //로그 등록
        } catch (RuntimeException e) {
            log.info("log 저장에 실패했습니다. logMessage={}", logMessage.getMessage());   //로그 등록 실패시 예외를 잡아서 처리(복구)
            log.info("정상 흐름 변환");
        }
        log.info("== logRepository 호출 종료 ==");
    }


}