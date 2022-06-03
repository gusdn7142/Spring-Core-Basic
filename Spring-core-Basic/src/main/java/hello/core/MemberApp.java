package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {


    public static void main(String[] args) {
        /*회원 가입 */
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();


        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);   //spring 컨테이너에 생성한 객체들을 넣어 관리
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);


        //MemberService memberService = new MemberServiceImpl();
        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        /* 회원 조회 */
        Member findMember = memberService.findMember(1l);
        System.out.println("new member = " + member.getName());
        System.out.println("find Member = " + findMember.getName());



    }



}
