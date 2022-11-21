package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {


    //1. 드라이버 매니저를 통해 커넥션 연결(+조회) 테스트
    @Test
    void driverManager() throws SQLException {

        //DriverManager로 커넥션 연결(+획득)
        Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);    //ConnectionConst의  URL, USERNAME, PASSWORD 상수를 가져와 h2 jdbc connection을 반환
        Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);    //ConnectionConst의  URL, USERNAME, PASSWORD 상수를 가져와 h2 jdbc connection을 반환

        log.info("connection={}, class={}", con1, con1.getClass());   //h2 DB connection1 정보 출력
        log.info("connection={}, class={}", con2, con2.getClass());   //h2 DB connection2 정보 출력
    }


    //2. 스프링이 제공하는 DataSource가 적용된 DriverManager : DriverManagerDataSource 사용
    @Test
    void dataSourceDriverManager() throws SQLException {

        //DataSource로 커넥션 연결
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL,USERNAME, PASSWORD);   //ConnectionConst의  URL, USERNAME, PASSWORD 상수를 가져와 h2 jdbc connection을 가진 DataSource 객체를 반환
        useDataSource(dataSource);
    }



    private void useDataSource(DataSource dataSource) throws SQLException {

        //DataSource로 커넥션 획득
        Connection con1 = dataSource.getConnection();   //DataSource에서 h2 jdbc connection을 반환
        Connection con2 = dataSource.getConnection();   //DataSource에서 h2 jdbc connection을 반환

        log.info("connection={}, class={}", con1, con1.getClass());   //h2 DB connection1 정보 출력
        log.info("connection={}, class={}", con2, con2.getClass());   //h2 DB connection2 정보 출력

    }








}