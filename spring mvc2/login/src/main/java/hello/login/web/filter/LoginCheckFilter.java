package hello.login.web.filter;


import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};   //홈 , 회원가입 , 로그인 , 로그아웃 uri , css uri


    //init()과 destroy()는 생략가능!!!
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;   //HttpServletRequest로 다운캐스팅
        String requestURI = httpRequest.getRequestURI();                //요청 uri를 불러옴.

        HttpServletResponse httpResponse = (HttpServletResponse) response;   //HttpServletResponse로 다운캐스팅

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);

            if (isLoginCheckPath(requestURI)) {   //화이트 리스트에 없는 경우
                log.info("인증 체크 로직 실행 {}", requestURI);

                HttpSession session = httpRequest.getSession(false);   //세션을 불러옴 (자동 생성X)

                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {  //세션이 null이면
                    log.info("미인증 사용자 요청 {}", requestURI);

                    //로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);   //로그인 후에 이전 페이지로 넘어가게 하기 위해 요청 uri도 같이 보냄
                    return;
                }
            }

            //requestURI가 화이트 리스트에 있는 경우
            chain.doFilter(request, response);  //다음 필터 혹은 컨트롤러 호출
        } catch (Exception e) {
            throw e; //예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 함
        } finally {
            log.info("인증 체크 필터 종료 {} ", requestURI);
        }

    }



    /**
     * 화이트 리스트의 경우 인증 체크X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);   //whitelist와 requestURI가 매칭되는지 확인 후 화이트리스트에 없으면 true 리턴
    }




}