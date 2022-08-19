package jpabook.jpashop.domain;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORDERS")   //order가 예약어이기 때문에 s를 붙입니다.
public class Order {


    @Id @GeneratedValue
    @Column(name="ORDER_ID")
    private Long id;

    @Column(name="MEMBER_ID")
    private Long memberId;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;





    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public java.time.LocalDateTime getLocalDateTime() {
        return orderDate;
    }

    public void setLocalDateTime(LocalDateTime orderDate) {
        orderDate = orderDate;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }





}
