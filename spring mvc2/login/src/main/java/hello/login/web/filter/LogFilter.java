package hello.login.web.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    //필터 초기화 메서드
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    //필터 로직 부분 (고객의 요청이 오면 해당 메서드 호출)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("log filter doFilter");



        HttpServletRequest httpRequest = (HttpServletRequest) request;  //ServletRequest의 자식인 HttpServletRequest로 다운 캐스팅
        String requestURI = httpRequest.getRequestURI();                //

        String uuid = UUID.randomUUID().toString();  //요청 구분을 위해 uuid 생성

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response);  //다음 필터 혹은 서블릿 호출
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }

    }

    //필터 종료 메서드
    @Override
    public void destroy() {
        log.info("log filter destroy");
    }



}