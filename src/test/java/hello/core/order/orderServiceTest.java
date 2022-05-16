package hello.core.order;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class orderServiceTest {

    //회원 서비스와 주문서비스 객체 생성
    MemberService memberService = new MemberServiceImpl();
    OrderService orderService = new OrderServiceImpl();

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


}