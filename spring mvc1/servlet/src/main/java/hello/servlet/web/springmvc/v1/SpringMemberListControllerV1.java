package hello.servlet.web.springmvc.v1;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SpringMemberListControllerV1 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/springmvc/v1/members")
    public ModelAndView process() {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //ModelAndView에 논리 뷰 이름 저장
        ModelAndView mv = new ModelAndView("members");

        //ModelAndView에 모든 회원 정보 저장
        mv.addObject("members", members);
        return mv;
    }





}
