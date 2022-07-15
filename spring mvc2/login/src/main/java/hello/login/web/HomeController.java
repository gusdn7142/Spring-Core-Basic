package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;



    //@GetMapping("/")
    public String home() {
        return "home";
    }


    //@GetMapping("/")
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



    //@GetMapping("/")
    public String homeLogin2(HttpServletRequest request,Model model) {

        //로그인 전  (로그아웃 포함)
        Member member = (Member)sessionManager.getSession(request);

        //로그인 성공시
        if (member == null) {
            return "home";
        }
        model.addAttribute("member", member);

        return "loginHome";
    }



    //@GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        //세션을 가져옴, 없으면 새로 생성하지 않고 null 반환
        HttpSession session = request.getSession(false);


        if (session == null) {  //세션이 없으면
            return "home";
        }

        Member loginMember = (Member)session.getAttribute(SessionConst.LOGIN_MEMBER);  //세션에서 값 (회원 객체)를 불러옴.

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }



    @GetMapping("/")
    public String homeLoginV3Spring(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
                                    Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);  //모델에 회원 객체 저장
        return "loginHome";
    }




}