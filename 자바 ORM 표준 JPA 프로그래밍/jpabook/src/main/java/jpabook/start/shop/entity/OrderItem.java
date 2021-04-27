package jpabook.start.shop.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Item item;
//    private Long itemId; / 잘못된 매핑

    @ManyToOne
    private Order order;
//    private Long orderPrice;
//    private Long count;
}
