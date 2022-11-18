package hello.jdbc.domain;

import lombok.Data;

@Data
public class Member {

    private String memberId;  //회원 id
    private int money;        //회원이 소지한 금액

    public Member() {
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }

}
