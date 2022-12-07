package hello.jdbc.exception.translator;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.exception.MyDbException;
import hello.jdbc.repository.exception.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {

    Repository repository;  //Repository 객체 선언
    Service service;        //Service 객체 선언

    //모든 테스트 시작전 실행
    @BeforeEach
    void init() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);   //항상 새로운 커넥션을 연결하는 DriverManagerDataSource 생성
        repository = new Repository(dataSource);          //Repository에 dataSource 의존성 주입
        service = new Service(repository);                //Service에 repository 의존성 주입
    }

    //같은 키 저장 테스트
    @Test
    void duplicateKeySave() {
        service.create("myId");
        service.create("myId");  //같은 ID 저장 시도
    }


    /*
     * Service 계층
     * repository.save() : memberId와 money 필드로 회원 등록
     * catch (MyDuplicateKeyException e) : 키 중복 예외(MyDuplicateKeyException)를 잡아서 랜덤 키를 만들고 회원 다시 등록
     * catch (MyDbException e) : MyDbException 예외를 다시 발생    //필요없는 로직
     */
    @Slf4j
    @RequiredArgsConstructor                  //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
    static class Service {
        private final Repository repository;   //Repository 객체 선언

        //회원 가입 로직
        public void create(String memberId) {
            try {
                repository.save(new Member(memberId, 0));   //회원 등록
                log.info("saveId={}", memberId);                   //회원 id 출력
            } catch (MyDuplicateKeyException e) {                 //MyDuplicateKeyException 예외를 잡음
                log.info("키 중복, 복구 시도");
                String retryId = generateNewId(memberId);        //memberId를 랜덤값으로 셋팅
                log.info("retryId={}", retryId);
                repository.save(new Member(retryId, 0));   //랜덤 memberId를 넣어 회원 등록 실행
            } catch (MyDbException e) {                         //MyDbException 예외를 잡음... 이 코드는 없어도 런타임 에러이기 때문에 자동으로 던짐..
                log.info("데이터 접근 계층 예외", e);
                throw e;                                        //MyDbException 에러 발생... 이 코드는 없어도 런타임 에러이기 때문에 자동으로 던짐..
            }
        }

        //memberId를 랜덤값으로 생성
        private String generateNewId(String memberId) {
            return memberId + new Random().nextInt(10000);   //10000이하의 랜덤 값
        }

    }

    /*
     * Repository 계층
     * pstmt.executeUpdate() : 회원 등록 로직 실행
     * catch (SQLException e) : SQLException을 잡아서 키 중복 오류일 경우 MyDuplicateKeyException 예외를 발생시키고 그게 아닐경우 MyDbException 예외 발생
     */
    @RequiredArgsConstructor                  //final이 붙거나 @NotNull 이 붙은 필드의 생성자를 자동 생성해주는 롬복 어노테이션
    static class Repository {
        private final DataSource dataSource;  //DataSource 객체 선언

        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";  //데이터베이스에 전달할 SQL을 정의
            Connection con = null;                                            //Connection 객체
            PreparedStatement pstmt = null;                                  //PreparedStatement 객체

            try {
                //Connection을 먼저 획득하고 Connection을 통해 PreparedStatement 생성 후 DB에 전달하여 ResultSet에 결과 값 반환, ResultSet의 값을 member 객체에 셋팅하여 반환
                con = dataSource.getConnection();              //DB Connection 연결 및 조회
                pstmt = con.prepareStatement(sql);            //sql로 PreparedStatement 객체
                pstmt.setString(1, member.getMemberId());   //SQL에서 첫번쨰 파라미터 바인딩
                pstmt.setInt(2, member.getMoney());         //SQL에서 두번쨰 파라미터 바인딩
                pstmt.executeUpdate();                                   //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 Select 쿼리의 결과이다.
                return member;
            } catch (SQLException e) {        //SQLException 체크 예외를 잡음
                //h2 db
                if (e.getErrorCode() == 23505) {           //예외외 오류 코드가 23505이면
                   throw new MyDuplicateKeyException(e);   //MyDuplicateKeyException 언 체크 예외로 전환
                }
                throw new MyDbException(e);                //MyDbException 언 체크 예외로 전환
            } finally {
                JdbcUtils.closeStatement(pstmt);   //PreparedStatement 연결 종료
                JdbcUtils.closeConnection(con);    //Connection 연결 종료
            }
        }
    }


}
