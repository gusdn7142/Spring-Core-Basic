package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 1.파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello&age=20&username=hello2
 */

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")  //서블릿 이름, url
public class RequestParamServlet extends HttpServlet {


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("[전체 파라미터 조회] - start");
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));  //key와 value 출력
        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();


        System.out.println("[단일 파라미터 조회] - start");
        String username = request.getParameter("username");  //username 파라미터 값 조회
        String age = request.getParameter("age");            //age 파라미터 값 조회

        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println("[단일 파라미터 조회] - end");
        System.out.println();

        System.out.println("[이름이 같은 복수 파라미터 조회] - start");
        String[] usernames = request.getParameterValues("username");  //getParameterValues 배열로 이름이 같은 파라미터 불러오기 가능
        for (String name : usernames) {
            System.out.println("username = " + name);
        }
        System.out.println("[이름이 같은 복수 파라미터 조회] - end");

        response.getWriter().write("ok");
    }



}
