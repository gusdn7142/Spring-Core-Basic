package hellojpa;


import javax.persistence.*;
import java.time.LocalDateTime;
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

    @ManyToOne(fetch = FetchType.LAZY)                     //하나의 팀이 여러명의 멤버를 가질 수 있음
    @JoinColumn(name = "TEAM_ID") //join하는 컬럼은 TEAM_ID  Team을 TEAM_ID로 join
    private Team team;

    //기간 : Period
    @Embedded
    private Period workPeriod;

    //주소
    @Embedded
    private Address homeAddress;

    //주소
//    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "city",
//                        column=@Column(name="WORK_CITY")),
//            @AttributeOverride(name = "street",
//                        column=@Column(name="WORK_STREET")),
//            @AttributeOverride(name = "zipcode",
//                        column=@Column(name="WORK_ZIPCODE"))
//            })
//    private Address workAddress;


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

    public Period getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Period workPeriod) {
        this.workPeriod = workPeriod;
    }


    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }



}
