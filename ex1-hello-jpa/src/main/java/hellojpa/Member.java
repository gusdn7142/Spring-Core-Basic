package hellojpa;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String userName;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne                     //하나의 팀이 여러명의 멤버를 가질 수 있음
    @JoinColumn(name = "TEAM_ID") //join하는 컬럼은 TEAM_ID  Team을 TEAM_ID로 join
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCEKR_ID")
    private Locker locker;

    @ManyToMany
    @JoinColumn(name = "MEMBER_PRODUCT")
    private List<Product> products = new ArrayList<>();




    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



}
