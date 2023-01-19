package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;


    //테스트1 - 회원과 로그정보 등록시 커밋 상황
    /**
     * MemberService @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON
     */
    @Test
    void outerTxOff_success() {

        //given
        String username = "outerTxOff_success";   //username 필드에 값 입력

        //when
        memberService.joinV1(username);   //회원 등록 & 로그 등록

        //then: 모든 데이터가 정상 저장
        assertTrue(memberRepository.find(username).isPresent());   //username을 통해 회원 조회
        assertTrue(logRepository.find(username).isPresent());      //username(==message필드값)을 통해 로그 조회

    }


    //테스트2 - 회원정보 등록시 커밋, 로그정보 등록시 롤백 상황
    /**
     * MemberService @Transactional:OFF
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOff_fail() {
        //given
        String username = "로그예외_outerTxOff_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);   //회원 등록 & 로그 등록(+RuntimeException예외 발생)

        //then: 완전히 롤백되지 않고, member 데이터가 남아서 저장된다.
        assertTrue(memberRepository.find(username).isPresent());   //username을 통해 회원 조회
        assertTrue(logRepository.find(username).isEmpty());      //username(==message필드값)을 통해 로그 조회.. 비어있음.
    }



    //테스트3 - 회원정보 등록시 커밋, 로그정보 등록시 롤백으로 인해 전체 롤백 상황 (MemberService에만  @Transactional 적용)
    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:OFF
     * LogRepository @Transactional:OFF
     */
    @Test
    void singleTx() {

        //given
        String username = "로그예외_singleTx";   //username 필드에 값 입력

        //when
        //memberService.joinV1(username);   //회원 등록 & 로그 등록
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);   //회원 등록 & 로그 등록(+RuntimeException예외 발생)

        //then: 모든 데이터가 정상 저장
        assertTrue(memberRepository.find(username).isEmpty());   //username을 통해 회원 조회.. 비어있음.
        assertTrue(logRepository.find(username).isEmpty());      //username(==message필드값)을 통해 로그 조회.. 비어있음.

    }


    //테스트4 - 회원정보 등록시 커밋, 로그정보 등록시 커밋 상황 (MemberService과 MemberRepository과 LogRepository에 @Transactional 적용)
    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON
     */
    @Test
    void outerTxOn_success() {
        //given
        String username = "outerTxOn_success";   //username 필드에 값 입력

        //when
        memberService.joinV1(username);   //회원 등록 & 로그 등록

        //then: 모든 데이터가 정상 저장된다.
        assertTrue(memberRepository.find(username).isPresent());   //username을 통해 회원 조회..
        assertTrue(logRepository.find(username).isPresent());      //username(==message필드값)을 통해 로그 조회.
    }



    //테스트5 - 회원정보 등록시 커밋, 로그정보 등록시 롤백으로 전체 롤백 상황 (MemberService과 MemberRepository과 LogRepository에 @Transactional 적용)
    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOn_fail() {

        //given
        String username = "로그예외_outerTxOn_fail";   //username 필드에 값 입력

        //when
        assertThatThrownBy(() -> memberService.joinV1(username)).isInstanceOf(RuntimeException.class);   //회원 등록 & 로그 등록(+RuntimeException예외 발생)

        //then: 모든 데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());  //username을 통해 회원 조회.. 비어있음.
        assertTrue(logRepository.find(username).isEmpty());     //username(==message필드값)을 통해 로그 조회.. 비어있음.
    }


    //테스트6 - 회원정보 등록시 커밋, 로그정보 등록시 롤백으로 전체 롤백 상황 (rollbackOnly==true, UnexpectedRollbackException 발생...  MemberService과 MemberRepository과 LogRepository에 @Transactional 적용)
    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional:ON Exception
     */
    @Test
    void recoverException_fail() {
        //given
        String username = "로그예외_recoverException_fail";

        //when
        assertThatThrownBy(() -> memberService.joinV2(username)).isInstanceOf(UnexpectedRollbackException.class);   //회원 등록 & 로그 등록(+RuntimeException예외를 복구).. 내부에 rollbackOnly=true 설정으로 인해 UnexpectedRollbackException 예외 발생

        //then: 모든 데이터가 롤백된다.
        assertTrue(memberRepository.find(username).isEmpty());  //username을 통해 회원 조회.. 비어있음.
        assertTrue(logRepository.find(username).isEmpty());     //username(==message필드값)을 통해 로그 조회.. 비어있음.
    }



    //테스트7 - 회원정보 등록시 커밋, 로그정보 등록시 롤백 상황 (로그 등록시 REQUIRES_NEW로 별도의 물리 트랜잭션을 적용, MemberService과 MemberRepository과 LogRepository에 @Transactional 적용)
    /**
     * MemberService @Transactional:ON
     * MemberRepository @Transactional:ON
     * LogRepository @Transactional(REQUIRES_NEW) Exception
     */
    @Test
    void recoverException_success() {

        //given
        String username = "로그예외_recoverException_success";

        //when
        memberService.joinV2(username);   //회원 등록 & 로그 등록(+RuntimeException예외를 복구)..

        //then: member 저장, log 롤백
        assertTrue(memberRepository.find(username).isPresent());   //username을 통해 회원 조회..
        assertTrue(logRepository.find(username).isEmpty());     //username(==message필드값)을 통해 로그 조회.. 비어있음.
    }



}