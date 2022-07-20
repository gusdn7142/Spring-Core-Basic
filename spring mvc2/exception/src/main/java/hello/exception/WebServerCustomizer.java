package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {


    //기본 오류 페이지 커스텀하는 함수
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        //404 오류 컨트롤러 호출 설정하는 에러페이지 정의
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");

        //500 오류 컨트롤러 호출 설정하는 에러페이지 정의
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        //런타임(+자식포함) 오류 컨트롤러 호출 설정하는 에러페이지 정의
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        //에러페이지 등록
        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }



}