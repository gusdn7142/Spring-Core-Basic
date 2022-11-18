package hello.jdbc.connection;


import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;


@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);  //ConnectionConst의  URL, USERNAME, PASSWORD 상수를 가져와 h2 jdbc connection을 반환
            log.info("get connection={}, class={}", connection, connection.getClass());   //h2 connection 정보 출력

            return connection;   //h2 connection 반환
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }


}