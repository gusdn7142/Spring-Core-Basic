package hello.core.order;

import hello.core.AppConfig;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import static org.assertj.core.api.Assertions.assertThat;


public class orderServiceTest {


    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach(){
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }


    //회원 서비스와 주문서비스 객체 생성
//    MemberService memberService = new MemberServiceImpl();
//    OrderService orderService = new OrderServiceImpl();



    @Test
    void createOrder() {

        //회원 ID 생성
        Long memberId = 1L;

        //회원 객체 생성
        Member member = new Member(memberId, "memberA", Grade.VIP);

        //회원 가입
        memberService.join(member);

        //주문 생성
        Order order = orderService.createOrder(memberId, "itemA", 10000);
        assertThat(order.getDiscountPrice()).isEqualTo(1000);  //할인금액으로 검증!!
    }



//    @Test
//    void fieldInjectionTest(){
//        OrderServiceImpl orderService = new OrderServiceImpl();
//        orderService.createOrder(1L, "itemA",10000);
//
//        orderService.setMemberRepository(new MemoryMemberRepository());
//        orderService.setDiscountPolicy(new FixDiscountPolicy());
//    }





}