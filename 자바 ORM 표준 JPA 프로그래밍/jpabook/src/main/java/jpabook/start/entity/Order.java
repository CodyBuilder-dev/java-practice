package jpabook.start.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

public class Order {
    private Long id;

    private Long memberId;

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}

enum OrderStatus{
    ORDER,CANCEL
}