package hello.servlet.web.servletmvc;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
public class MvcMemberSaveServlet extends HttpServlet {

    /* MemberRepository 객체 셍성 */
    private MemberRepository memberRepository = MemberRepository.getInstance();    //싱글톤 패턴(private)이기 때문에 직접 접근 불가

    @Override //상속 필수
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //요청으로 받은 파라미터 값 ("이름", "나이") 조회
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 데이터를 보관 (jsp에서 request객체로 데이터를 조회하기 위함)
        request.setAttribute("member", member);

        //회원가입 응답 페이지 호출
        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);   //save-result.jsp 호출
    }


}