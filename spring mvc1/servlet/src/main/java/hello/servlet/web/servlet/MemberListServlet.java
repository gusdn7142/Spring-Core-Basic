package hello.servlet.web.servlet;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {

    /* MemberRepository 객체 셍성 */
    //private MemberRepository memberRepository = new MemberRepository();  //싱글톤 패턴(private)이기 때문에 직접 접근 불가
    private MemberRepository memberRepository = MemberRepository.getInstance();



    @Override   //상속 필수
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //응답메시지에 헤더정보 설정
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        //응답메시지에 출력할 값 셋팅
        PrintWriter w = response.getWriter();
        w.write("<html>");
        w.write("<head>");
        w.write("    <meta charset=\"UTF-8\">");
        w.write("    <title>Title</title>");
        w.write("</head>");

        w.write("<body>");
        w.write("<a href=\"/index.html\">메인</a>");
        w.write("<table>");
        w.write("    <thead>");
        w.write("    <th>id</th>");
        w.write("    <th>username</th>");
        w.write("    <th>age</th>");
        w.write("    </thead>");
        w.write("    <tbody>");

        //동적 html 문자열 생성
        for (Member member : members) {   //members 리스트 객체에서 member객체를 하나씩 꺼냄
            w.write("    <tr>");
            w.write("        <td>"+member.getId()+"</td>");
            w.write("        <td>"+member.getUsername()+"</td>");
            w.write("        <td>"+member.getAge()+"</td>");
            w.write("    </tr>");
        }

        w.write("    </tbody>");
        w.write("</table>");
        w.write("</body>");
        w.write("</html>");
    }







}
