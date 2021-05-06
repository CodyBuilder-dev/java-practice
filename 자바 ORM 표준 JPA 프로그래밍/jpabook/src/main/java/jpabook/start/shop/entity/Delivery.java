package jpabook.start.shop.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Delivery extends DateInfo{

    @Id
    private long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    public Order getOrder() { return order; }

    public void setOrder(Order order) {
        if (this.order == order) // 순환 참조 방지
            return;
        if (this.order != null) // 기존 관계 제거
            this.order.setDelivery(null);

        this.order = order;
        order.setDelivery(this);
    }

    private String city;

    private String street;

    private String zip;

    private DeliveryStatus status;
}

enum DeliveryStatus{
    PREPARE,ONBOARD,COMPLETE,CANCEL
}