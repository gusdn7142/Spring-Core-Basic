package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{



    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;


    //@Autowired    //생성자가 1개이기 때문에 생략 가능!!!
    public OrderServiceImpl(MemberRepository memberRepository,@MainDiscountPolicy DiscountPolicy discountPolicy) {
        //System.out.println(memberRepository);
        //System.out.println(discountPolicy);
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    @Override
    //주문 생성
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);   //회원 조회
        int discountPrice = discountPolicy.discount(member, itemPrice);  //할인 금액 조회


        //주문 객체 생성후 리턴
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }







//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        //System.out.println(memberRepository);
//        this.memberRepository = memberRepository;
//    }
////
//
//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy){
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//
//    }
//
//
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        //System.out.println(discountPolicy);
//        this.discountPolicy = discountPolicy;
//    }




//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();









    //테스트 코드
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }



}