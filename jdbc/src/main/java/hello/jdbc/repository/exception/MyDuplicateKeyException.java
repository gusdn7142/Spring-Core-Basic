package hello.jdbc.repository.exception;


//데이터 중복 예외
public class MyDuplicateKeyException extends MyDbException {   //RuntimeException을 상속받는 MyDbException을 상속 받음

    //기본 생성자
    public MyDuplicateKeyException() {
    }

    //생성자 1 : 에러메시지 포함
    public MyDuplicateKeyException(String message) {
        super(message);
    }

    //생성자2 : 에러메시지와 하위 예외 포함
    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    //생성자3 : 하위 예외 포함
    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
