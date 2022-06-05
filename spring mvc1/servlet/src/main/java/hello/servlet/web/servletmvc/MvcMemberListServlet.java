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
import java.util.List;

@WebServlet(name = "mvcMemberListServlet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet {

    /* MemberRepository 객체 셍성 */
    private MemberRepository memberRepository = MemberRepository.getInstance(); //싱글톤 패턴(private)이기 때문에 직접 접근 불가


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //Model에 모든 회원 정보 저장   (jsp에서 request객체로 데이터를 조회하기 위함)
        request.setAttribute("members", members);  //key, value

        //전체 회원목록 조회 페이지 호출
        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }



}