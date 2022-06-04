package hello.servlet.web.servlet;


import hello.servlet.domain.member.MemberRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "memberFormServlet", urlPatterns = "/servlet/members/new-form")
public class MemberFormServlet extends HttpServlet {

    /* MemberRepository 객체 셍성 */
    //private MemberRepository memberRepository = new MemberRepository();  //싱글톤 패턴(private)이기 때문에 직접 접근 불가
    private MemberRepository memberRepository = MemberRepository.getInstance();


    @Override //상속 필수
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {  //HttpServlet 클래스르 상속받아 service() 함수를 불러와 재정의

        //Header의 ContentType과 인코딩 지정
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");


        //응답메시지에 출력할 form 파라미터 문자열 생성
        PrintWriter w = response.getWriter();
        w.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"/servlet/members/save\" method=\"post\">\n" +    //요청할 url : /servlet/members/save
                "    username: <input type=\"text\" name=\"username\" />\n" +
                "    age:      <input type=\"text\" name=\"age\" />\n" +
                "    <button type=\"submit\">전송</button>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>\n");
    }






}
