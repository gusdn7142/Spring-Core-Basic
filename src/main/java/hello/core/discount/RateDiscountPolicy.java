package hello.core.discount;


import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.stereotype.Component;


@Component
public class RateDiscountPolicy implements  DiscountPolicy{

    private int discountPercent = 10;  //핳인 퍼센트

    @Override
    /* 할인 금액 반환 */
    public int discount(Member member, int price) {
        if (member.getGrade() == Grade.VIP) {
            return price * discountPercent / 100;  //금액 * 할인퍼센트 / 100
        }
        return 0;
    }



}
