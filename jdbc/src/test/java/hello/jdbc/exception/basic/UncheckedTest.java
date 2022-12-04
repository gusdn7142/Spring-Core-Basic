package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UncheckedTest {

    //1. Service 계층에서 언체크 예외를 잡아서 처리(catch)하는 테스트
    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();   //callCatch() 메서드 호출
    }

    //2. Service 계층에서 언체크 예외를 던지지(throws) 않아도 자동으로 던져지는지 테스트
    @Test
    void unchecked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())       //callThrow() 메서드 호출... Service 계층에서 넘어온 MyCheckedException 예외를 던지거나 잡지 않아도 컴파일 에러가 발생하지 않습니다.
                .isInstanceOf(MyUncheckedException.class);   //MyUncheckedException 예외가 터지는지 확인...   이 예외를 잡지 않으면  WAS에게 예외를 넘겨주어 에러 메시지가 콘솔에 출력됩니다.
    }

    /**
     * MyUncheckedException : RuntimeException을 상속받은 예외는 언체크 예외가 됩니다.
     */
    static class MyUncheckedException extends RuntimeException {   //RuntimeException 예외를 상속받은 언체크 예외 클래스
        public MyUncheckedException(String message) {
            super(message);     //에러 메시지를 상위 클래스에 전달
        }
    }


    /**
     * Service 계층
     * callCatch() : 언체크 예외를 잡아서 처리
     * callThrow() : 언쳬크 예외를 잡지 않아도 Controller에게 던짐
     */
    static class Service {
        Repository repository = new Repository();

        public void callCatch() {
            try {
                repository.call();       //call()메서드 호출.... Repository에서 넘어온 MyUncheckedException 예외를 던지거나 잡지 않아도 컴파일 에러가 발생하지 않습니다.
            } catch (MyUncheckedException e) {    //MyUncheckedException 예외를 잡아서 처리
                //예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        public void callThrow() {
            repository.call();       //call()메서드 호출.... Repository에서 넘어온 MyUncheckedException 예외를 던지거나 잡지 않아도 컴파일 에러가 발생하지 않습니다.
        }
    }


    /**
     * Repository 계층
     * MyUncheckedException 예외 발생 후 예외를 던지지(throws) 않아도 Service 계층에 자동으로 던져짐...
     */
    static class Repository {
        public void call() {
            throw new MyUncheckedException("ex");   //언체크 예외는 던지거나(throws) 잡지(catch) 않아도 에러가 발생하지 않고... 자동으로 던져짐.
        }
    }

}
