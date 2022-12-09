package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;


/**
 * JdbcTemplate 사용
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{


    private final JdbcTemplate template;                 //JdbcTemplate 생성
    //private final DataSource dataSource;               //DataSource객체 생성
    //private final SQLExceptionTranslator exTranslator;   //스프링 예외 추상화 (SQLExceptionTranslator) 객체 생성


    //DataSource 의존관계 주입
    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        //this.dataSource = dataSource;
        //this.exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);   //SQLExceptionTranslator 인터페이스의 구현체로 SQLErrorCodeSQLExceptionTranslator(dataSource)을 주입
    }


    /**
     * 1. 회원 등록 함수
     */
    @Override
    public Member save(Member member)  {  //throws SQLException
        String sql = "insert into member(member_id, money) values (?, ?)";   //데이터베이스에 전달할 SQL을 정의

        template.update(sql, member.getMemberId(), member.getMoney());      //SQL 실행...  리턴 값은 Update된 row 수이다..
        return member;                                                        //member객체 반환


//        Connection con = null;            //Connection 객체
//        PreparedStatement pstmt = null;  //PreparedStatement 객체
//
//        try {
//            //Connection을 먼저 획득하고 Connection을 통해 PreparedStatement 생성 후 DB에 전달
//            con = getConnection();               //DB Connection 조회
//            pstmt = con.prepareStatement(sql);   //sql로 PreparedStatement 객체 생성 : 데이터베이스에 전달할 SQL과 파라미터로 전달할 데이터들을 준비
//
//            pstmt.setString(1, member.getMemberId());   //SQL에서 첫번쨰 파라미터 바인딩
//            pstmt.setInt(2, member.getMoney());   //SQL에서 두번쨰 파라미터 바인딩
//            pstmt.executeUpdate();                            //PreparedStatement 실행 : 준비된 SQL을 커넥션을 통해 실제 데이터베이스에 전달...  반환 값은 영향받은 DB row 수 이다... (생략 가능)
//            return member;
//        } catch (SQLException e) {
//            throw exTranslator.translate("save", sql, e);   //SQL을 구문 분석하여 해당되는 (런타임)  스프링 추상화 예외 (ex, )를 리턴
//            //throw new MyDbException(e);
//            //log.error("db error", e);
//        } finally {
//            //리소스 정리 : 반환할 때는 PreparedStatement를 먼저 종료하고, 그 다음에 Connection을 종료...    리소스 정리는 필수입니다!!!
//            close(con, pstmt, null);   //Connection과  PreparedStatement 연결 종료
//        }
    }


    private RowMapper<Member> memberRowMapper() {  //SELECT 쿼리에 들어갈 칼럼 매핑 정보
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));   //member_id 칼럼을 member의 memberId 필드에 저장
            member.setMoney(rs.getInt("money"));             //money 칼럼을 member의 money 필드에 저장
            return member;
        };
    }

    private RowMapper<Member> memberRowMapper2() {  //SELECT 쿼리에 들어갈 칼럼 매핑 정보
        return (rs, rowNum) -> new Member(
                rs.getString("member_id"),   //member_id 칼럼을 member의 memberId 필드에 저장
                rs.getInt("money"));             //money 칼럼을 member의 money 필드에 저장
    }

    /**
     * 2. 회원 조회 (MemberId 활용)
     */
    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id= ?";   //데이터베이스에 전달할 SQL을 정의

        return template.queryForObject(sql, memberRowMapper2(), memberId);        //한 건 조회 SQL 실행...  (sql 구문, SELECT 쿼리에 들어갈 칼럼 매핑 정보 , 파라미터)



    }





    /**
     * 3. 회원 정보 변경 함수
     */
    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";   //데이터베이스에 전달할 SQL을 정의
        template.update(sql, money, memberId);                            //SQL 실행(sql 구문, 파라미터)...  리턴 값은 Update된 row 수이다..
    }




    /**
     * 4. 회원 정보 삭제 함수
     */
    @Override
    public void delete(String memberId){
        String sql = "delete from member where member_id=?";   //데이터베이스에 전달할 SQL을 정의
        template.update(sql, memberId);                       //SQL 실행(sql 구문, 파라미터)...  리턴 값은 Update된 row 수이다..

    }




//    private void close(Connection con, Statement stmt, ResultSet rs) {  //ResultSet은 select 쿼리에서 사용됩니다.
//
//        JdbcUtils.closeResultSet(rs);     //ResultSet 연결 종료
//        JdbcUtils.closeStatement(stmt);  //PreparedStatement 연결 종료
//
//        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용 필요
//        DataSourceUtils.releaseConnection(con, dataSource);    //동기화된 커넥션은 커넥션을 닫지 않고 그대로 유지 or 동기화 매니저가 관리하는 커넥션이 없는 경우 해당 커넥션을 닫음
//        //JdbcUtils.closeConnection(con);  //Connection 연결 종료
//
//    }


//    private Connection getConnection() throws SQLException {
//
//        //주의! 트랜잭션 동기화를 사용하려면 DataSourceUtils 사용 필요
//        Connection con = DataSourceUtils.getConnection(dataSource);   //트랜잭션 동기화 매니저에서 커넥션 획득 or 동기화 매니저가 관리하는 커넥션이 없는 경우 새로운 커넥션을 생성해서 반환
//        log.info("get connection={}, class={}", con, con.getClass());
//        return con;
//    }


}
