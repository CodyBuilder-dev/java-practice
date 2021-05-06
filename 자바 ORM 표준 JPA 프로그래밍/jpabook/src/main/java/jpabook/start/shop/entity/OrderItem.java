package jpabook.start.shop.entity;

import javax.persistence.*;

@Entity
public class OrderItem extends DateInfo{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    private Item item;
//    private Long itemId; // 데이터베이스 중심의 매핑

    @ManyToOne
    private Order order;
//    private Long orderPrice;
//    private Long count;
}
