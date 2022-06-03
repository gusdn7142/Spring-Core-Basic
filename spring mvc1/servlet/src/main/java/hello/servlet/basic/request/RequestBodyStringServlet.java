package hello.servlet.basic.request;


import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")  //서블릿 이름, url
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //요청시 입력받은 MessageBody 정보 출력
        ServletInputStream inputStream = request.getInputStream();  //body 요청 정보를 바이트로 변환
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);  //바이트를 문자열로 변환시 인코딩 정보를 알려줘야함

        System.out.println("messageBody = " + messageBody);  //messageBody 출력

        response.getWriter().write("ok");   //응답 정보에 "ok"를 넣음.


    }



}
