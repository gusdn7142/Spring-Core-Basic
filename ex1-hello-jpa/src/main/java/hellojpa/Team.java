package hellojpa;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {   //부모 클래스


    @Id
    @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;


    @OneToMany                   //mappedBy를 통해 어떤거와 매핑되어 있는지를 알려준다!
    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<Member>();   //ArrayList를 사용하는것이 관례 (add할떄 Null 포인트를 막기 위함)






    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}