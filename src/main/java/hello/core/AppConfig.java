package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration   //설정 정보 파일에 적용
public class AppConfig {

    //@Bean memberService -> new MemberMemoryRepository()
    //@Bean orderService -> new MemberMemoryRepository()



    //회원 서비스
    @Bean  //스프링 컨테이너에 등록됨.
    public MemberService memberService() {
        System.out.println("호출 : AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("호출 : AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    //주문 서비스
    @Bean
    public OrderService orderService(){
        System.out.println("호출 : AppConfig.memberService");
        return new OrderServiceImpl(memberRepository(), discountPolicy() );
//        return null;

    }

    @Bean
    public DiscountPolicy discountPolicy() {
//        retrun new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }



}
