package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //@GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId,
                            Model model) {

        //로그인 전  (로그아웃 포함)
        if (memberId == null) {
            return "home";
        }

        //로그인 성공시
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {   //쿠키에 담긴 회원 ID에 해당하는 회원 객체가 없으면
            return "home";
        }
        model.addAttribute("member", loginMember);
        return "loginHome";
    }






}