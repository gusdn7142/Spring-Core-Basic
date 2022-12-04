package hello.jdbc.exception.basic;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;



public class CheckedAppTest {

    //Repository와 NetworkClient -> Service -> Controller 계층에 던져진 2개의 체크 예외를 공통 클래스(Exception)로 처리하는 테스트
    @Test
    void checked() {
        Controller controller = new Controller();

        assertThatThrownBy(() -> controller.request())    //request() 메서드 호출... Controller 계층에서 넘어온 SQLException과 ConnectException 체크 예외를 던지거나 잡아야 컴파일 에러가 발생하지 않습니다.
                .isInstanceOf(Exception.class);         //SQLException과  ConnectException 예외가 터지는지 Exception 공통 클래스로 확인
    }

    /**
     * 4. Controller 계층
     * SQLException과 ConnectException 체크 예외를 Controller 밖으로 던짐
     */
    static class Controller {

        Service service = new Service();

        public void request() throws SQLException, ConnectException {     //체크 예외는 던지거나 잡아서 처리하지 않으면 컴파일 에러가 발생합니다.
            service.logic();     //서비스 계층의 logic() 메서드 호출...
        }
    }

    /**
     * 3. Service 계층
     * repository.call() : SQLException 체크 예외를 Controller에게 다시 던짐
     * networkClient.call() : ConnectException 체크 예외를 Controller에게 다시 던짐
     */
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException {     //체크 예외는 던지거나 잡아서 처리하지 않으면 컴파일 에러가 발생합니다.
            repository.call();
            networkClient.call();
        }

    }

    /**
     * 1. Repository 계층
     * SQLException 체크 예외 발생 후 Service 계층에 던짐
     */
    static class Repository {
        public void call() throws SQLException {      //체크 예외는 던지거나 잡아서 처리하지 않으면 컴파일 에러가 발생합니다.
            throw new SQLException("ex");
        }
    }

    /**
     * 2. NetworkClient 계층
     * ConnectException 체크 예외 발생 후 Service 계층에 던짐
     */
    static class NetworkClient {
        public void call() throws ConnectException {     //체크 예외는 던지거나 잡아서 처리하지 않으면 컴파일 에러가 발생합니다.
            throw new ConnectException("연결 실패");
        }
    }




}
