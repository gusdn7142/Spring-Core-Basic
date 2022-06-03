package hello.servlet.basic;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "helloServlet", urlPatterns ="/hello")  //localhost:8080/hello로 접속시 이게 실행된다.
public class HelloServlet extends HttpServlet {   //HttpServlet을 상속받는다.


    @Override  //서블릿 호출시 serice()가 호출됨           (단축키 :단축키 :ctrl+o로 함수 만듦)
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");         //단축키 :sout (m)
        System.out.println("request = " + request);         //단축키 :sout (v)
        System.out.println("response = " + response);       //단축키 :sout (v)

        //요청 파라미터 출력
        String username = request.getParameter("username");  //쿼리 스트링 파라미터 조회
        System.out.println("username = " + username);      //ctrl+alt+v ?? ㄴㄴ

        //응답 메시지 담기
        response.setContentType("text/plain");  //문자열 형식 지정 (header에 contentType에 들어감)
        response.setCharacterEncoding("utf-8"); //인코딩 설정     (header의 contentType에 들어감)
        response.getWriter().write("hello " + username);      //response body에 들어감




    }






}
