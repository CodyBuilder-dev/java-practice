package jpabook.start.shop.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class Order {
    @Id
    private Long id;

    private Long memberId;

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        if (this.member != null){
            this.member.getOrders().remove(this);
        }
        this.member = member;
        member.getOrders().add(this);
    }


    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

}

enum OrderStatus{
    ORDER,CANCEL
}