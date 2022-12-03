package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


@Slf4j
public class CheckedTest {

    //1. Service 계층에서 체크 예외를 잡아서 처리(catch)하는 테스트
    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();   //callCatch() 메서드 호출
    }


    //2. Service 계층에서 체크 예외를 다시 던지는(throws) 테스트
    @Test
    void checked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())   //callThrow() 메서드 호출... Service 계층에서 넘어온 MyCheckedException 예외를 던지거나 잡아야 컴파일 에러가 발생하지 않습니다.
                .isInstanceOf(MyCheckedException.class);   //MyCheckedException 예외가 터지는지 확인
    }


    /**
     * MyCheckedException : Exception을 상속받은 예외는 체크 예외가 됩니다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }



    /**
     * Service 계층
     * callCatch() : 체크 예외를 잡아서 처리
     * callThrow() : 쳬크 예외를 잡아서 Controller에게 다시 던짐
     */
    static class Service {
        Repository repository = new Repository();

        //쳬크 예외를 잡아서 처리
        public void callCatch() {
            try {
                repository.call();   //call()메서드 호출.... Repository에서 넘어온 MyCheckedException 예외를 던지거나 잡아야 컴파일 에러가 발생하지 않습니다.
            } catch (MyCheckedException e) {  //MyCheckedException 예외 처리
                log.info("예외 처리, message={}", e.getMessage(), e);  //Exception 정보도 출력
            }
        }

        //쳬크 예외를 잡아서 Controller에게 다시 던짐
        public void callThrow() throws MyCheckedException {
            repository.call();     //call()메서드 호출.... Repository에서 넘어온 MyCheckedException 예외를 던지거나 잡아야 컴파일 에러가 발생하지 않습니다.
        }
    }



    /**
     * Repository 계층
     * MyCheckedException 예외 발생 후 Servicee 계층에 던짐
     */
    static class Repository {
        public void call() throws MyCheckedException {   //체크 예외는 던지거나 잡아서 처리하지 않으면 컴파일 에러가 발생합니다.
            throw new MyCheckedException("ex");
        }
    }


}