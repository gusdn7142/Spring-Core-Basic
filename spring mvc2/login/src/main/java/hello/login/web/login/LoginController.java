package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;



    //(전) 로그인
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }


    //(후) 로그인
    //@PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {  //오류가 발생하면 같은 뷰 리턴
            return "login/loginForm";
        }

        //로그인 서비스 로직 실행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);


        //loginMember가 null이면 같은 뷰 리턴
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리

        //쿠키에 시간 정보를 주지 않으면 세션 쿠키 (브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId",String.valueOf(loginMember.getId()));   //쿠키 생성
        response.addCookie(idCookie);  //응답 객체에 쿠키 포함


        return "redirect:/";

    }


    //(후) 로그인
    //@PostMapping("/login")
    public String login2(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {  //오류가 발생하면 같은 뷰 리턴
            return "login/loginForm";
        }

        //로그인 서비스 로직 실행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);


        //loginMember가 null이면 같은 뷰 리턴
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리


        //세션 관리자를 통해 세션을 생성 + 세션 저장소에 세션 저장 + 세션 쿠키 생성
        sessionManager.createSession(loginMember, response);

        return "redirect:/";

    }


    //(후) 로그인
    //@PostMapping("/login")
    public String login3(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest request) {

        if (bindingResult.hasErrors()) {  //오류가 발생하면 같은 뷰 리턴
            return "login/loginForm";
        }

        //로그인 서비스 로직 실행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);


        //loginMember가 null이면 같은 뷰 리턴
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리



        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);



        return "redirect:/";

    }



    //(후) 로그인
    @PostMapping("/login")
    public String login4(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult
                         ,@RequestParam(defaultValue = "/") String redirectURL
                         ,HttpServletRequest request) {

        if (bindingResult.hasErrors()) {  //오류가 발생하면 같은 뷰 리턴
            return "login/loginForm";
        }

        //로그인 서비스 로직 실행
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());
        log.info("login? {}", loginMember);


        //loginMember가 null이면 같은 뷰 리턴
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }
        //로그인 성공 처리



        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();

        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);




        return "redirect:" + redirectURL;

    }







    //로그아웃
    //@PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }



    //로그아웃
    //@PostMapping("/logout")
    public String logout2(HttpServletRequest request) {

        sessionManager.expire(request);
        return "redirect:/";
    }

    //로그아웃
    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);  //기존 세션 반환, 없으면 없으면 새로 생성하지 않고 null 반환

        if (session != null) {
            session.invalidate();   //세션과 데이터가 모두 날아감.
        }

        return "redirect:/";
    }




    //쿠키 만료 함수
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }


}