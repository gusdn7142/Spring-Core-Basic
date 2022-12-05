package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    //Repository와 NetworkClient -> Service -> Controller 계층에 던져진 2개의 언체크 예외를 공통 클래스(Exception)로 처리하는 테스트
    @Test
    void unchecked() {
        Controller controller = new Controller();

        assertThatThrownBy(() -> controller.request())    //request() 메서드 호출... Controller 계층에서 넘어온 SQLException과 ConnectException 언체크 예외를 던지거나 잡지 않아도 컴파일 에러가 발생하지 않습니다.
                .isInstanceOf(Exception.class);         //RuntimeSQLException과  RuntimeConnectException 언 체크 예외가 터지는지 Exception 공통 클래스로 확인
    }


    /*
     *(런타임 => 체크) 예외 전환 테스트
     * : RuntimeSQLException과 RuntimeConnectException 언체크 예외를 Controller 밖으로 던짐
     */
    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
//            e.printStackTrace();
            //System.out.println(e);
            log.info("Exception={}",e);
        }

    }



    /**
     * 4. Controller 계층
     * RuntimeSQLException과 RuntimeConnectException 언 체크 예외를 Controller 밖으로 던짐
     */
    static class Controller {

        Service service = new Service();

        public void request(){
            service.logic();     //서비스 계층의 logic() 메서드 호출...
        }
    }

    /**
     * 3. Service 계층
     * repository.call() : RuntimeSQLException 언체크 예외를 Controller에게 자동으로 던짐
     * networkClient.call() : RuntimeConnectException 언체크 예외를 Controller에게 자동으로 던짐
     */
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }

    }

    /**
     * 1. Repository 계층
     * call() : SQLException 체크 예외 발생시 잡아서 RuntimeSQLException로 전환 후 Service 계층에 자동 던짐
     * runSQL() : SQLException 체크 예외 발생 후 던짐
     */
    static class Repository {
        public void call(){
            try{
                runSQL();
            }catch (SQLException e){
                throw new RuntimeSQLException(e);      //RuntimeSQLException 언체크 예외 발생
            }
        }

        public void runSQL() throws SQLException{      //체크 예외는 던지거나 잡아서 처리하지 않으면 컴파일 에러가 발생합니다.
            throw new SQLException("ex");       //SQLException 체크 예외 발생
        }
    }


    /**
     * 2. NetworkClient 계층
     * RuntimeConnectException 언체크 예외 발생 후 Service 계층에 자동 던짐
     */
    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }


    //RuntimeException을 상속받는 RuntimeConnectException 커스텀 예외 클래스
    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    //RuntimeException을 상속받는 RuntimeSQLException 커스텀 예외 클래스
    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() { }

        public RuntimeSQLException(Throwable cause) {   //Throwable 객체를 통해 이 전 예외 객체 포함 가능!
            super(cause);
        }
    }






}
