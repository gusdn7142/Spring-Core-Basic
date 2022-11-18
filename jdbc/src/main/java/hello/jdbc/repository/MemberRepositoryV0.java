package hello.jdbc.repository;

import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;


/**
 * JDBC - DriverManager 사용
 */
@Slf4j
public class MemberRepositoryV0 {

    /**
     * 1. 회원 등록 함수
     */
    public Member save(Member member) throws SQLException {
        String sql = "insert into member(member_id, money) values (?, ?)";   //데이터베이스에 전달할 SQL을 정의

        Connection con = null;            //Connection 객체
        PreparedStatement pstmt = null;  //PreparedStatement 객체

        try {
            //Connection을 먼저 획득하고 Connection을 통해 PreparedStatement 생성 후 DB에 전달
            con = getConnection();               //DB Connection 조회
            pstmt = con.prepareStatement(sql);   //sql로 PreparedStatement 객체 생성 : 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비

            pstmt.setString(1, member.getMemberId());   //SQL에서 첫번쨰 파라미터 바인딩
            pstmt.setInt(2, member.getMoney());   //SQL에서 두번쨰 파라미터 바인딩
            pstmt.executeUpdate();                            //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 영향받은 DB row 수 이다... (생략 가능)
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            //리소스 정리 : 반환할 때는 PreparedStatement를 먼저 종료하고, 그 다음에 Connection을 종료...    리소스 정리는 필수입니다!!!
            close(con, pstmt, null);   //Connection과  PreparedStatement 연결 종료
        }
    }


    private void close(Connection con, Statement stmt, ResultSet rs) {  //ResultSet은 select 쿼리에서 사용됩니다.

        if (rs != null) {
            try {
                rs.close();     //ResultSet 연결 종료
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();     //PreparedStatement 연결 종료
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();      //Connection 연결 종료
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

    }


    private Connection getConnection() {
        return DBConnectionUtil.getConnection();  //DB Connection 조회
    }


}
