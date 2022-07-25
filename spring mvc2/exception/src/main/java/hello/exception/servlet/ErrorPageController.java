package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

    //RequestDispatcher 상수로 정의되어 있음
    public static final String ERROR_EXCEPTION = "javax.servlet.error.exception";
    public static final String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    public static final String ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    public static final String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";


    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 404");

        printErrorInfo(request);
        return "error-page/404";  //"error-page/404" 뷰 호출
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("errorPage 500");

        printErrorInfo(request);

        return "error-page/500"; //"error-page/500" 뷰 호출
    }



    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)  //produces : 서버가 클라이언트에게 반환하는 데이터 타입 명시
    public ResponseEntity<Map<String, Object>> errorPage500Api( HttpServletRequest request,
                                                                HttpServletResponse response) {

        log.info("API errorPage 500");

        Map<String, Object> result = new HashMap<>();

        Exception ex = (Exception) request.getAttribute(ERROR_EXCEPTION);   //request객체에서 exception 객체 불러오기
        result.put("status", request.getAttribute(ERROR_STATUS_CODE));   //result map에 request 객체에서 불러온 http Error status Code 입력
        result.put("message", ex.getMessage());   //result map에 오류 메시지 입력
        //result.put("message2", request.getAttribute(ERROR_MESSAGE));   //result map에 오류 메시지 입력

        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);  //상태코드

        return new ResponseEntity<>(result, HttpStatus.valueOf(statusCode));   //map 객체 result와 http status code 반환
    }









    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }


}
