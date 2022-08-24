package jpabook.jpashop.domain;


import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)  //생략 가능
    @Column(name="MEMBER_ID")
    private Long id;

    private String name;


    private String city;


    private String street;


    private String zipcode;

    @OneToMany(mappedBy = "member")    //mappedBy : 연관관계의 주인 설정
    private List<Order> orders = new ArrayList<Order>();



    //getter() setter()
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipzode() {
        return zipcode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setZipzode(String zipcode) {
        this.zipcode = zipcode;
    }








}
