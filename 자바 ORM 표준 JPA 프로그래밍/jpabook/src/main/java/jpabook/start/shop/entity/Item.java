package jpabook.start.shop.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;
    private Long price;
    private Long stockQuantity;

    // 비즈니스 요건상, 상품에서 주문상품을 조회할 일이 없으면 굳이 양방향 필요 없음
    //@OneToMany(mappedBy = "item" )
}


