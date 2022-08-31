package jpabook.jpashop.domain;


import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member extends BaseEntity  {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)  //생략 가능
    @Column(name="MEMBER_ID")
    private Long id;
    private String name;

    @Embedded     //생략가능
    private Address address;



    @OneToMany(mappedBy = "member")    //mappedBy : 연관관계의 주인 설정
    private List<Order> orders = new ArrayList<Order>();



    //getter() setter()
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }



}
