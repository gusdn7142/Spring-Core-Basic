package hello.itemservice.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity                 //JPA가 사용하는 객체라는 뜻
//@Table(name = "item")  //객체 명과 같으면 생략 가능
public class Item {

    @Id                                                   //PK 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)    //PK 전략 설정
    private Long id;

    @Column(name = "item_name", length = 10)
    private String itemName;   //아이템명

    //필드명과 칼럼명이 같으면 @Column 생략가능
    private Integer price;     //가격

    //필드명과 칼럼명이 같으면 @Column 생략가능
    private Integer quantity;   //수량

    //JPA는 public 또는 protected의 기본 생성자가 필수
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
