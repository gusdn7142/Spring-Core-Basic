package hello.core;


import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.Order;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class OrderApp {

    public static void main(String[] args) {

//        MemberService memberService = new MemberServiceImpl();
//        OrderService orderService = new OrderServiceImpl();



        //회원 서비스와 주문서비스 객체 생성
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();



        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);   //spring 컨테이너에 생성한 객체들을 넣어 관리
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);






        //회원 ID 생성
        Long memberId = 1L;

        //회원 객체 생성
        Member member = new Member(memberId, "memberA", Grade.VIP);

        //회원 가입
        memberService.join(member);

        //주문 생성성
        Order order = orderService.createOrder(memberId, "itemA", 20000);
        System.out.println("order = " + order);






    }









}
