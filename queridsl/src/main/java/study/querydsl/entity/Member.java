package study.querydsl.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   //기본생성자 생성 (다른 패키지에 소속된 클래스는 접근 불가)
@ToString(of = {"id", "username", "age"})            //team은 제외 (무한루프 방지)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;



    //생성자
    public Member(String username) {
        this(username, 0);
    }
    //생성자
    public Member(String username, int age) {
        this(username, age, null);
    }
    //생성자
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    //편의 메소드
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }




}