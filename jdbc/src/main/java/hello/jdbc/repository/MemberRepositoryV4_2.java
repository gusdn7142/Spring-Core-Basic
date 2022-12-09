package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.exception.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;


/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{


    private final DataSource dataSource;               //DataSource객체 생성
    private final SQLExceptionTranslator exTranslator;   //스프링 예외 추상화 (SQLExceptionTranslator) 객체 생성


    //DataSource 의존관계 주입
    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);   //SQLExceptionTranslator 인터페이스의 구현체로 SQLErrorCodeSQLExceptionTranslator(dataSource)을 주입
    }


    /**
     * 1. 회원 등록 함수
     */
    @Override
    public Member save(Member member)  {  //throws SQLException
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
            throw exTranslator.translate("save", sql, e);   //SQL을 구문 분석하여 해당되는 (런타임)  스프링 추상화 예외 (ex, )를 리턴
            //throw new MyDbException(e);
            //log.error("db error", e);
        } finally {
            //리소스 정리 : 반환할 때는 PreparedStatement를 먼저 종료하고, 그 다음에 Connection을 종료...    리소스 정리는 필수입니다!!!
            close(con, pstmt, null);   //Connection과  PreparedStatement 연결 종료
        }
    }

    /**
     * 2. 회원 조회 (MemberId 활용)
     */
    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id= ?";   //데이터베이스에 전달할 SQL을 정의

        Connection con = null;            //Connection 객체
        PreparedStatement pstmt = null;  //PreparedStatement 객체
        ResultSet rs = null;             //ResultSet 객체

        try {
            //Connection을 먼저 획득하고 Connection을 통해 PreparedStatement 생성 후 DB에 전달하여 ResultSet에 결과 값 반환, ResultSet의 값을 member 객체에 셋팅하여 반환
            con = getConnection();               //DB Connection 연결 및 조회
            pstmt = con.prepareStatement(sql);   //sql로 PreparedStatement 객체 생성 : 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비
            pstmt.setString(1, memberId);   //SQL에서 첫번쨰 파라미터 바인딩

            rs = pstmt.executeQuery();        //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 Select 쿼리의 결과이다.
            if (rs.next()) {   //ResultSet 객체에서 Row 값을 하나씩 조회
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));  //ResultSet 객체에서 memberId 필드를 조회하여 member 객체에 셋팅
                member.setMoney(rs.getInt("money"));  //ResultSet 객체에서 money 필드를 조회하여 member 객체에 셋팅
                return member;                                    // member 객체 리턴
            } else {
                throw new NoSuchElementException("member not found memberId=" + memberId);  //ResultSet의 객체가 1개도 없다면
            }

        } catch (SQLException e) {
            throw exTranslator.translate("findById", sql, e);   //SQL을 구문 분석하여 해당되는 (런타임)  스프링 추상화 예외 (ex, BadSqlGrammarException)를 리턴
            //throw new MyDbException(e);
            //log.error("db error", e);
        } finally {
            //리소스 정리 : 반환할 때는 PreparedStatement를 먼저 종료하고, 그 다음에 Connection을 종료...    리소스 정리는 필수입니다!!!
            close(con, pstmt, rs);     //Connection과  PreparedStatement, ResultSet 연결 종료
        }

    }





    /**
     * 3. 회원 정보 변경 함수
     */
    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";   //데이터베이스에 전달할 SQL을 정의

        Connection con = null;            //Connection 객체 생성
        PreparedStatement pstmt = null;   //PreparedStatement 객체 생성

        try {
            //Connection을 먼저 획득하고 Connection을 통해 PreparedStatement 생성 후 DB에 전달
            con = getConnection();               //DB Connection 연결 및 조회
            pstmt = con.prepareStatement(sql);   //sql로 PreparedStatement 객체 생성 : 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비

            pstmt.setInt(1, money);          //SQL에서 첫번쨰 파라미터 바인딩
            pstmt.setString(2, memberId);   //SQL에서 두번쨰 파라미터 바인딩
            int resultSize = pstmt.executeUpdate();      //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 영향받은 DB row 수 이다...

            log.info("resultSize={}", resultSize);
        } catch (SQLException e) {
            //log.error("db error", e);
            throw exTranslator.translate("update", sql, e);   //SQL을 구문 분석하여 해당되는 (런타임)  스프링 추상화 예외 (ex, )를 리턴
            //throw new MyDbException(e);
        } finally {
            //Connection 연결 종료는... Service 로직에서 필요!!!
            //리소스 정리 : 반환할 때는 PreparedStatement를 먼저 종료하고, 그 다음에 Connection을 종료...    리소스 정리는 필수입니다!!!
            close(con, pstmt, null);
        }

    }




    /**
     * 4. 회원 정보 삭제 함수
     */
    @Override
    public void delete(String memberId){
        String sql = "delete from member where member_id=?";   //데이터베이스에 전달할 SQL을 정의

        Connection con = null;            //Connection 객체 생성
        PreparedStatement pstmt = null;   //PreparedStatement 객체 생성

        try {
            //Connection을 먼저 획득하고 Connection을 통해 PreparedStatement 생성 후 DB에 전달
            con = getConnection();               //DB Connection 연결 및 조회
            pstmt = con.prepareStatement(sql);   //sql로 PreparedStatement 객체 생성 : 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비

            pstmt.setString(1, memberId);          //SQL에서 첫번쨰 파라미터 바인딩
            pstmt.executeUpdate();                             //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 영향받은 DB row 수 이다...
        } catch (SQLException e) {
            throw exTranslator.translate("delete", sql, e);   //SQL을 구문 분석하여 해당되는 (런타임) 스프링 추상화 예외 (ex, )를 리턴
            //log.error("db error", e);
            //throw new MyDbException(e);
        } finally {
            //리소스 정리 : 반환할 때는 PreparedStatement를 먼저 종료하고, 그 다음에 Connection을 종료...    리소스 정리는 필수입니다!!!
            close(con, pstmt, null);
        }

    }




    private void close(Connection con, Statement stmt, ResultSet rs) {  //ResultSet은 select 쿼리에서 사용됩니다.

        JdbcUtils.closeResultSet(rs);     //ResultSet 연결 종료
        JdbcUtils.closeStatement(stmt);  //PreparedStatement 연결 종료

        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용 필요
        DataSourceUtils.releaseConnection(con, dataSource);    //동기화된 커넥션은 커넥션을 닫지 않고 그대로 유지 or 동기화 매니저가 관리하는 커넥션이 없는 경우 해당 커넥션을 닫음
        //JdbcUtils.closeConnection(con);  //Connection 연결 종료

    }


    private Connection getConnection() throws SQLException {

        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용 필요
        Connection con = DataSourceUtils.getConnection(dataSource);   //트랜잭션 동기화 매니저에서 커넥션 획득 or 동기화 매니저가 관리하는 커넥션이 없는 경우 새로운 커넥션을 생성해서 반환
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }


}
