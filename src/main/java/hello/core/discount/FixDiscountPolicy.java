package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;

public class FixDiscountPolicy implements DiscountPolicy{


    private int discountFIxAmount = 1000; // 1000원 할인

    @Override
    //할인 금액 반환
    public int discount(Member member, int price) {  //discount() 함수 구현
        if (member.getGrade() == Grade.VIP) {  //회원 등급이 vip이면
            return discountFIxAmount;   //1000원 반환
        }
        return 0;   //아니면 0원 반환
    }


}
