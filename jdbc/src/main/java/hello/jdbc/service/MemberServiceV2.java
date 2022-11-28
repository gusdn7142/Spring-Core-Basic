package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * 트랜잭션 - 파라미터 연동, 풀을 고려한 종료
 */
@Slf4j
@RequiredArgsConstructor    //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
public class MemberServiceV2 {

    private final DataSource dataSource;                 //DataSource 객체 선언
    private final MemberRepositoryV2 memberRepository;   //MemberRepositoryV1 생성자 주입


    //계좌이체 함수 : 2명의 회원간에 돈 거래
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection con = dataSource.getConnection();   //DataSource에서 커넥션 연결(+획득)
        try {
            con.setAutoCommit(false);                 //(수동커밋 모드로) 트랜잭션 시작

            //비즈니스 로직 수행 부분
            Member fromMember = memberRepository.findById(con, fromId);  //보내는 이
            Member toMember = memberRepository.findById(con, toId);      //받는 이

            //계좌이체 로직 (+예외 발생 포함)
            memberRepository.update(con, fromId, fromMember.getMoney() - money);   //보내는이의 돈에서 money를 뺴준는 Update 쿼리 수행
            validation(toMember);                                                   //받는이의 memberId가 "ex"일떄 예외를 발생시켜 정상적인 계좌이체를 실패시킴
            memberRepository.update(con, toId, toMember.getMoney() + money);   //받는이의 돈에서 money를 더해주는 Update 쿼리 수행

            con.commit();      //수동 커밋 실행

        } catch (Exception e) {
            con.rollback();    //비즈니스 로직 에러 발생시 롤백
            throw new IllegalStateException(e);   //예외 던지기
        } finally {
            if (con != null) {               //커넥션이 null이 아니면
                try {
                    con.setAutoCommit(true); //자동 커밋모드로 설정..  커넥션 풀을 사용할 경우 추후 커넥션이 살아있기 때문에 변경해 주는 것..
                    con.close();             //커넥션 연결 종료 (커넥션 풀을 사용할 경우 풀로 돌아감)
                } catch (Exception e) {
                    log.info("error", e);
                }
            }

        }
    }


    //memberID가 "ex"인 회원이 있으면 IllegalStateException 예외 발생
    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
