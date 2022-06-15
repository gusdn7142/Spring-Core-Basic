package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//SLF4J 사용
@Slf4j    //로그 선언을 자동으로 해줌 (EX, private final Logger log = LoggerFactory.getLogger(getClass());
@RestController  //Controller + Responsebody
public class LogTestController {

    //로그 선언 : etClass()메서드를 통해 사용되는 클래스 타입 반환하여 삽입
    //private final Logger log = LoggerFactory.getLogger(getClass());
    //== private static final Logger log = LoggerFactory.getLogger(Xxx.class);

    //log 테스트 API
    @RequestMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        //log.trace("trace my log=" + name);   //해당 연산은 컴파일 시점에 문자열+변수 연산이 수행되기 떄문에 리소스 낭비가 발생합니다.

        //출력문자열 안에 변수를 넣을떄 { }를 사용하는것을 권장
        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info(" info log={}", name);
        log.warn(" warn log={}", name);
        log.error("error log={}", name);

        return "ok";
    }

}