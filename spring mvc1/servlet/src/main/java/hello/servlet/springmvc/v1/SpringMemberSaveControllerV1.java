package hello.servlet.springmvc.v1;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class SpringMemberSaveControllerV1 {


    private MemberRepository memberRepository = MemberRepository.getInstance();


    @RequestMapping("/springmvc/v1/members/save")
    public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {

        //요청으로 받은 파라미터 값 ("이름", "나이") 조회
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
        Member member = new Member(username, age);
        memberRepository.save(member);

        //ModelAndView에 논리 뷰 이름 저장
        ModelAndView mv = new ModelAndView("save-result");

        //ModelAndView에 회원 정보 저장
        mv.addObject("member", member);
        return mv;


    }





}
