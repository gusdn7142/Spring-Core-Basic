package hello.exception.api;


import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class ApiExceptionV2Controller {


//    //컨트롤러에서 발생한 IllegalArgumentException를 잡는다.
//    @ResponseStatus(HttpStatus.BAD_REQUEST)   //상태코드 지정 (지정 안하면 200으로 나감)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalExHandler(IllegalArgumentException e){
//        log.error("[exceptionHandler] ex", e);
//
//        return new ErrorResult("BAD", e.getMessage());
//    }




//    //컨트롤러에서 발생한 UserException을 잡는다.
//    @ExceptionHandler                 //생략가능 : (UserException.class)
//    public ResponseEntity<ErrorResult> userExHandler(UserException e){
//        log.error("[exceptionHandler] ex", e);
//
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
//
//    }



//    //컨트롤러에서 발생한 Exception을 잡는다.   (자식들까지 처리 가능!!)
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)   //상태코드 지정 (지정 안하면 200으로 나감)
//    @ExceptionHandler                 //생략가능 : (Exception.class)
//    public ErrorResult exHandler(Exception e){
//        log.error("[exceptionHandler] ex", e);
//
//        return new ErrorResult("EX", "내부 오류");
//    }




    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }
        if (id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }
        if (id.equals("user-ex")) {
            throw new UserException("사용자 오류");
        }

        return new MemberDto(id, "hello " + id);
    }






    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }




}
