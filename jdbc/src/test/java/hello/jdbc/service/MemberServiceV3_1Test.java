package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV2;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * 트랜잭션 - 트랜잭션 매니저
 */
@Slf4j
class MemberServiceV3_1Test {

    //상수 선언
    public static final String MEMBER_A = "memberA";
    public static final String MEMBER_B = "memberB";
    public static final String MEMBER_EX = "ex";

    //회원 서비스와 레포지토리 객체 선언
    private MemberRepositoryV3 memberRepository;
    private MemberServiceV3_1 memberService;


    //각 테스트 실행전에 먼저 실행됨 : DB와의 커넥션 연결 및 서비스와 레포지토리의 의존성 주입
    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);   //항상 새로운 커넥션을 연결하는 DriverManagerDataSource 생성
        memberRepository = new MemberRepositoryV3(dataSource);                //의존성 주입

        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);   //DataSourceTransactionManager 객체 생성
        memberService = new MemberServiceV3_1(transactionManager, memberRepository);    //의존성 주입
    }

    //각 테스트가 실행된 후에 실행됨 : DB에서 MEMBER_A, MEMBER_B, MEMBER_EX 레코드 삭제
//    @AfterEach
//    void after() throws SQLException {
//        memberRepository.delete(MEMBER_A);   //DB에서 member_id가 "memberA"인 row를 삭제
//        memberRepository.delete(MEMBER_B);   //DB에서 member_id가 "memberB"인 row를 삭제
//        memberRepository.delete(MEMBER_EX);   //DB에서 member_id가 "ex"인 row를 삭제
//    }


    @Test
    @DisplayName("정상 계좌이체")
    void accountTransfer() throws SQLException {

        //given : 데이터를 저장해서 테스트를 준비
        Member memberA = new Member(MEMBER_A, 10000);   //회원 객체(memberA) 생성
        Member memberB = new Member(MEMBER_B, 10000);   //회원 객체(memberB) 생성
        memberRepository.save(memberA);                        //회원 객체(memberA)를 DB에 저장
        memberRepository.save(memberB);                        //회원 객체(memberB)를 DB에 저장

        //when : 계좌이체 로직을 실행
        log.info("START TX");
        memberService.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);  //DB에서 memberA의 money를 2000 줄이고, memberB의 money를 2000 늘린다..
        log.info("END TX");

        //then : 계좌이체가 정상 수행되었는지 검증
        Member findMemberA = memberRepository.findById(memberA.getMemberId());      //회원 객체(memberA)의 id를 통해 회원A 조회
        Member findMemberB = memberRepository.findById(memberB.getMemberId());      //회원 객체(memberB)의 id를 통해 회원B 조회
        assertThat(findMemberA.getMoney()).isEqualTo(8000);                         //회원A의 돈이 정상적으로 2000원 줄어들어 8000원이 되었는지 검증
        assertThat(findMemberB.getMoney()).isEqualTo(12000);                        //회원B의 돈이 정상적으로 2000원 늘어나 12000원이 되었는지 검증
    }


    @Test
    @DisplayName("예외 발생 계좌이체")
    void accountTransferEx() throws SQLException {

        //given : 데이터를 저장해서 테스트를 준비
        Member memberA = new Member(MEMBER_A, 10000);   //회원 객체(memberA) 생성
        Member memberEx = new Member(MEMBER_EX, 10000); //회원 객체(MEMBER_EX) 생성
        memberRepository.save(memberA);                        //회원 객체(memberA)를 DB에 저장
        memberRepository.save(memberEx);                       //회원 객체(MEMBER_EX)를 DB에 저장

        //when : 계좌이체 로직을 실행 (+IllegalStateException 예외가 발생하는지 검증)
        assertThatThrownBy(() -> memberService.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))  //DB에서 memberA의 money를 2000 줄이고, memberEx의 money를 2000 늘린다..
                .isInstanceOf(IllegalStateException.class);   //IllegalStateException 예외가 발생하는지 검증


        //then : 계좌이체가 정상 수행되었는지 검증
        Member findMemberA = memberRepository.findById(memberA.getMemberId());      //회원 객체(memberA)의 id를 통해 회원A 조회
        Member findMemberEX = memberRepository.findById(memberEx.getMemberId());      //회원 객체(memberEx)의 id를 통해 회원B 조회
        assertThat(findMemberA.getMoney()).isEqualTo(10000);                          //회원A의 돈이 롤백으로 인해 2000원이 줄어들지 않게 되어 그대로 10000원이 되었는지 검증
        assertThat(findMemberEX.getMoney()).isEqualTo(10000);                          //회원EX의 돈이 예외가 발생하여 2000원 늘어나지 않게 되어 그대로 10000원이 되었는지 검증
    }


}