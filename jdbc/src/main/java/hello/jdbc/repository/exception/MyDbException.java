package hello.jdbc.repository.exception;


public class MyDbException extends RuntimeException {   //RuntimeException 언체크 예외 상속

    //기본 생성자
    public MyDbException() {
    }

    //생성자 1 : 에러메시지 포함
    public MyDbException(String message) {
        super(message);
    }

    //생성자2 : 에러메시지와 하위 예외 포함
    public MyDbException(String message, Throwable cause) {
        super(message, cause);
    }

    //생성자3 : 하위 예외 포함
    public MyDbException(Throwable cause) {
        super(cause);
    }
}
