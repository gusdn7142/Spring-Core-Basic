package hello.springtx.order;


//커스텀 체크 예외 (비즈니스 예외)
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}