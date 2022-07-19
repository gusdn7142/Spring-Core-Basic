package hello.login.web.interceptor;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {


    //컨트롤러 호출 전 호출
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();  //uri를 불러옴.

        log.info("인증 체크 인터셉터 실행 {}", requestURI);  //uri 출력

        HttpSession session = request.getSession(false);   //세션을 불러옴.

        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {   //세션이 없으면
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);  //로그인 후에 이전 페이지로 넘어가게 하기 위해 요청 uri도 같이 보냄
            return false;   //다음 인터셉트 혹은 컨트롤러를 호출하지 않음.
        }

        return true;   //true이면 다음 인터셉터 혹은 컨트롤러 호출
    }


    //postHandle()과 afterCompletion()은 생략



}
