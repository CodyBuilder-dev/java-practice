package jpabook.start.shop.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Order {
    @Id
    private Long id;

    private Long memberId;

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private Member member;

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


    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }


    @OneToOne
    private Delivery delivery;

    public Delivery getDelivery() { return delivery; }

    public void setDelivery(Delivery delivery) {
        if (this.delivery == delivery)
            return;
        if (this.delivery != null)
            this.delivery.setOrder(null);

        this.delivery = delivery;
        delivery.setOrder(this);
    }

}

enum OrderStatus{
    ORDER,CANCEL
}