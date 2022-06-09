package hello.servlet.web.frontcontroller.v5;


import hello.servlet.web.frontcontroller.ModelView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyHandlerAdapter {


    //어댑터가 해당 컨트롤러 터압과 맞는지 확인 후 true or false 반환
    boolean supports(Object handler);


    //컨트롤러를 호출하고, 그 결과로 ModelView를 반환
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;



}
