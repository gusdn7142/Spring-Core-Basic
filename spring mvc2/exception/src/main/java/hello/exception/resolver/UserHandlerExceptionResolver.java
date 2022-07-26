package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        try {

            if (ex instanceof UserException) {   //UserException을 가져와서 처리
                log.info("UserException resolver to 400");

                String acceptHeader = request.getHeader("accept");   //accept 헤더의 값 추출
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);    //응답을 400으로 설정


                if ("application/json".equals(acceptHeader)) {   //Accept가 "application/json" 이면

                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());          //errorResult map에 에러 클래스 입력
                    errorResult.put("message", ex.getMessage());  //errorResult map에 에러 메시지 입력

                    String result = objectMapper.writeValueAsString(errorResult);   //writeValueAsString() : String 타입으로 변환

                    response.setContentType("application/json");   //응답시 헤더 타입을 "application/json'으로 설정
                    response.setCharacterEncoding("utf-8");        //응답시 인코딩 설정
                    response.getWriter().write(result);            //errorResult를 json 형식으로 반환

                    return new ModelAndView();                     //새로운 ModelAndView 반환
                } else {  //Accept가 TEXT/HTML일 경우
                    return new ModelAndView("error/500");   //ModelAndView 반환
                }
            }

        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        return null;
    }
}
