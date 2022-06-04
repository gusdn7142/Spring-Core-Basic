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

@WebServlet(name = "memberSaveServlet", urlPatterns = "/servlet/members/save")
public class MemberSaveServlet extends HttpServlet {

    /* MemberRepository 객체 셍성 */
    //private MemberRepository memberRepository = new MemberRepository();  //싱글톤 패턴(private)이기 때문에 직접 접근 불가
    private MemberRepository memberRepository = MemberRepository.getInstance();


    @Override //상속 필수
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("MemberSaveServlet.service");

        //요청으로 받은 파라미터 값 ("이름", "나이") 조회
        String username = request.getParameter("username");  //getParameter는 GET 쿼리스트링 혹은 From 파라미터 모두 조회 가능
        int age = Integer.parseInt(request.getParameter("age"));  //문자열을 int형으로 변환도 해주어야함


        //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
        Member member = new Member(username, age);
        memberRepository.save(member);

        //응답할 헤더 설정 (ContentType, 인코딩 형식)
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");


        //응답메시지에 출력할 문자열 생성
        PrintWriter w = response.getWriter();
        w.write("<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "성공\n" +
                "<ul>\n" +
                "    <li>id="+member.getId()+"</li>\n" +              //회원 객체의 id 출력
                "    <li>username="+member.getUsername()+"</li>\n" +  //회원 객체의 이름 출력
                "    <li>age="+member.getAge()+"</li>\n" +            //회원 객체의 나이 출력
                "</ul>\n" +
                "<a href=\"/index.html\">메인</a>\n" +
                "</body>\n" +
                "</html>");
    }







}
