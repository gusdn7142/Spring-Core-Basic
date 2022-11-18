package hello.jdbc.connection;

public abstract class ConnectionConst {   //상수이기 때문에 abstract로 객체 생성 방지
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";  //H2 DB 연결 주소
    public static final String USERNAME = "sa";        //H2 사용자 계정 정보
    public static final String PASSWORD = "";
}