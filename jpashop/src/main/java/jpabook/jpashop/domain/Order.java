package jpabook.jpashop.domain;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "ORDERS")   //order가 예약어이기 때문에 s를 붙입니다.
public class Order extends BaseEntity  {


    @Id @GeneratedValue
    @Column(name="ORDER_ID")
    private Long id;

//    @Column(name="MEMBER_ID")
//    private Long memberId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = ALL)    //mappedBy : 연관관계의 주인 설정
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();


    @OneToOne(fetch = LAZY, cascade = ALL)
    @JoinColumn(name="DELIVERY_ID")
    private Delivery delivery;


    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;



//    //연관관계 편의 메서드
//    public void addOrderItem(OrderItem orderItem){
//        orderItems.add(orderItem);  //부모인 Order 엔티티의 orderItems 객체에 값(orderItem) 삽입,
//        orderItem.setOrder(this);    //자식인 OrderItem 엔티티의 order 객체에 값(this=order) 삽입
//    }









}
