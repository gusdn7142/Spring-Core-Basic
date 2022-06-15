package hello.springmvc.basic.requestmapping;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class MappingController {

    //SLF4J 선언
    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 기본 요청
     * 둘다 허용 /hello-basic, /hello-basic/
     * HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping(value = "/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "ok";
    }

    /**
     * HTTP 메서드 요청만 허용
     * GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }




    /**
     * 편리한 축약 애노테이션 (코드보기)
     * @GetMapping : Get 요청
     * @PostMapping : Post 요청
     * @PutMapping : Put 요청
     * @DeleteMapping : Delete 요청
     * @PatchMapping : Patch 요청
     */
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }



    /**
     * PathVariable (경로 변수) 사용
     * 변수명이 같으면 생략 가능
     *
     * @PathVariable("userId") String userId -> @PathVariable userId
     * /mapping/userA
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }




    /**
     * @PathVariable 다중 사용
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }



    /**
     * 특정 파라미터 조건 매핑 (잘 사용하지 않음)
     * params="mode",  //mode라는 이름이 있어야 함
     * params="!mode"  //mode라는 이름이 없어야 함
     * params="mode=debug"  //key, value 모두 필요
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }


    /**
     * 특정 헤더의 조건 매핑
     * headers="mode",       //mode라는 이름이 있어야 함
     * headers="!mode"       //mode라는 이름이 없어야 함
     * headers="mode=debug"  //key, value 모두 필요
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }


    /**
     * 미디어 타입 조건 매핑 - HTTP 요청시 헤더의 Content-Type인 consume 설정
     * consumes="application/json"  //header의 content-type이 applicaion-json일 경우에만 호출됨
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)  //application/json
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }



    /**
     * 미디어 타입 조건 매핑 - HTTP 요청시 헤더의 Accept인 produce 설정
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
}