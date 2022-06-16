package hello.servlet.springmvc.v2;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 클래스 단위 -> 메서드 단위
 * @RequestMapping 클래스 레벨과 메서드 레벨 조합
 */

@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {

    //싱글톤 객체 생성
    private MemberRepository memberRepository = MemberRepository.getInstance();


    //회원 가입 요청
    @RequestMapping("/new-form")
    public ModelAndView newForm() {

        //ModelAndView에 논리 뷰 이름 리턴
        return new ModelAndView("new-form");
    }



    //회원 가입 응답
    @RequestMapping("/save")
    public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {

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



    //전체 회원 목록 조회
    @RequestMapping
    public ModelAndView members() {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //ModelAndView에 논리 뷰 이름 저장
        ModelAndView mv = new ModelAndView("members");

        //ModelAndView에 회원 정보 저장
        mv.addObject("members", members);
        return mv;
    }






}
