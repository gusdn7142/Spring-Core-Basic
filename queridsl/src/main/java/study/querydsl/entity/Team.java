package study.querydsl.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)   //기본생성자 생성 (다른 패키지에 소속된 클래스는 접근 불가)
@ToString(of = {"id", "name"})                       //members은 제외 (무한루프 방지)
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    //생성자
    public Team(String name) {
        this.name = name;
    }
}