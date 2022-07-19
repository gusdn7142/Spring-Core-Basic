package hello.login.web.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;



@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String LOG_ID = "logId";

    //컨트롤러 호출 전 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();   //uri를 불러옴
        String uuid = UUID.randomUUID().toString();   //uuid 생성

        request.setAttribute(LOG_ID, uuid);           //request 객체에 uuid 저장

        //@RequestMapping: HandlerMethod
        //정적 리소스: ResourceHttpRequestHandler
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;     //호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true;  //true이면 다음 인터셉터 혹은 컨트롤러 호출
    }


    //컨트롤러 호출 후 호출
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }


    //뷰가 렌더링된 이후 호출
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();             //uri를 불러옴
        String logId = (String) request.getAttribute(LOG_ID);    //uuid로그를 불러옴
        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);   //uri와 uuid 로그, handler 출력

       if (ex != null) {  //예외가 있을시
            log.error("afterCompletion error!!", ex);
        }

    }


}
