package jpabook.jpashop.domain;


import net.bytebuddy.dynamic.loading.InjectionClassLoader;

import javax.persistence.*;


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
