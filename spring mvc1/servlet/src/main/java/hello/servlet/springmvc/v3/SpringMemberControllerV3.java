package hello.servlet.springmvc.v3;


import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    //싱글톤 객체 생성
    private MemberRepository memberRepository = MemberRepository.getInstance();

    //회원 가입 요청
//    @RequestMapping(value = "", method = RequestMethod.GET)
    @GetMapping("/new-form")
    public String newForm() {

        //논리 뷰 이름 리턴
        return "new-form";
    }

    //회원 가입 응답
//    @RequestMapping(value = "", method = RequestMethod.POST)
    @PostMapping("/save")
    public String save(
            @RequestParam("username") String username,  //파라미터 값 username 받기
            @RequestParam("age") int age,               //파라미터 값 age 받기
            Model model) {

        //회원객체 생성 & 파라미터 값 삽입 & 회원 저장소에 저장
        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 회원 정보 저장
        model.addAttribute("member", member);

        //논리 뷰 이름 리턴
        return "save-result";
    }


    //전체 회원 목록 조회
//    @RequestMapping(value = "", method = RequestMethod.GET)
    @GetMapping
    public String members(Model model) {

        //모든 회원 정보 조회
        List<Member> members = memberRepository.findAll();

        //Model에 회원 정보 저장
        model.addAttribute("members", members);

        //논리 뷰 이름 리턴
        return "members";
    }




}
