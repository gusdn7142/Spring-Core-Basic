package hello.springmvc.basic.request;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;


@Slf4j   //다음 코드 자동생성 : private static final Logger log = LoggerFactory.getLogger(RequestHeaderController.class);
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,        //request 객체
                          HttpServletRequest response,       //response 객체
                          HttpMethod httpMethod,             //http method
                          Locale locale,                     //locale 정보
                          @RequestHeader MultiValueMap<String, String> headerMap,       //header 전체 받기
                          @RequestHeader("host") String host,                            //header 1개 받기
                          @CookieValue(value = "myCookie", required = false) String cookie   //cookie 받기
    ) {
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";





    }







}
