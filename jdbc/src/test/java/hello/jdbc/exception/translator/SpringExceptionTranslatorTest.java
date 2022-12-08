package hello.jdbc.exception.translator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;



/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;

    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);     //항상 새로운 커넥션을 연결하는 DriverManagerDataSource 생성
    }

    //예외 변환기 사용X, ErrorCode 사용
    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammar";  //데이터베이스에 전달할 SQL을 정의

        try {
            Connection con = dataSource.getConnection();          //DB Connection 연결 및 조회
            PreparedStatement stmt = con.prepareStatement(sql);  //sql로 PreparedStatement 객체 생성
            stmt.executeQuery();                                  //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 Select 쿼리의 결과이다.
        } catch (SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(42122);   //errorcode로 "42122"가 나오는지 확인
            int errorCode = e.getErrorCode();                 //errorcode로 조회
            log.info("errorCode={}", errorCode);              //errorcode를 로그로 출력
            log.info("error", e);                            //error 내용 로그로 출력
        }
    }


    //예외 변환기 사용
    @Test
    void exceptionTranslator() {
        String sql = "select bad grammar";  //데이터베이스에 전달할 SQL을 정의

        try {
            Connection con = dataSource.getConnection();          //DB Connection 연결 및 조회
            PreparedStatement stmt = con.prepareStatement(sql);  //sql로 PreparedStatement 객체 생성
            stmt.executeQuery();                                  //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 Select 쿼리의 결과이다
        } catch (SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(42122);      //errorcode로 "42122"가 나오는지 확인


            //SQL 예외 변환기 (SQLErrorCodeSQLExceptionTranslator)가  SQL을 분석하여 BadSqlGrammarException 예외 생성
            SQLErrorCodeSQLExceptionTranslator exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);      //SQLErrorCodeSQLExceptionTranslator : sql-error-codes.xml 파일을 파싱해서 각각의 DB가 제공하는 SQLException의 ErrorCode에 맞는 적절한 스프링 데이터 접근 예외로 변환하는 역할을 함
            DataAccessException resultEx = exTranslator.translate("select", sql, e);   //"설명", 실행된 SQL, SQLException
            log.info("resultEx", resultEx);                                                 //DataAccessException 로그로 출력
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);      //DataAccessException이 BadSqlGrammarException인지 확인
        }

    }
}
