package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;


/**
 * 트랜잭션 - 트랜잭션 템플릿
 */
@Slf4j
//@RequiredArgsConstructor    //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MemberServiceV3_2 {

    //private final DataSource dataSource;                         //DataSource 객체 선언
    //private final PlatformTransactionManager transactionManager;  //PlatformTransactionManager 객체 선언   (DataSourceTransactionManager 구현체를 주입 받아 사용).
    private final TransactionTemplate txTemplate;                   //TransactionTemplate객체 선언   (PlatformTransactionManager를 주입 받아 사용).
    private final MemberRepositoryV3 memberRepository;   //MemberRepositoryV3 생성자 주입


    //생성자
    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
        this.txTemplate = new TransactionTemplate(transactionManager);    //TransactionTemplate객체 초기화   (PlatformTransactionManager를 주입 받아 사용).
        this.memberRepository = memberRepository;
    }


    //계좌이체 함수 : 2명의 회원간에 돈 거래
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        txTemplate.executeWithoutResult((status) -> {   //트랜잭션 시작  (+커넥션 획득, 트랜잭션 동기화 매니저에 커넥션 보관).... 코드가 정상적으로 다 실행되면 커밋(+자동커밋모드로 변환, 커넥션 종료)하고 예외가 발생하면 롤백(+자동커밋모드로 변환, 커넥션 종료)한다.
            try {
                //비즈니스 로직 수행 부분
                Member fromMember = memberRepository.findById(fromId);  //보내는 이
                Member toMember = memberRepository.findById(toId);      //받는 이

                //계좌이체 로직 (+예외 발생 포함)
                memberRepository.update(fromId, fromMember.getMoney() - money);   //보내는이의 돈에서 money를 뺴준는 Update 쿼리 수행
                validation(toMember);                                                   //받는이의 memberId가 "ex"일떄 예외를 발생시켜 정상적인 계좌이체를 실패시킴
                memberRepository.update(toId, toMember.getMoney() + money);   //받는이의 돈에서 money를 더해주는 Update 쿼리 수행
            }
            catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }


    //memberID가 "ex"인 회원이 있으면 IllegalStateException 예외 발생
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
