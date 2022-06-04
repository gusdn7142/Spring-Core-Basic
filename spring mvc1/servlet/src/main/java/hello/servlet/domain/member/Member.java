package hello.servlet.domain.member;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {

    private Long id;          //기본키
    private String username;   //이름
    private int age;           //나이


    //기본 생성자
    public Member() {
    }

    //args 생성자
    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }



}
