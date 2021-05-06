package jpabook.start.shop.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ALBUM")
public class Album extends Item { // Item은 Dateinfo를 상속받는데, 그렇다고 Album도 Dateinfo를 상속받는 것은 아니다
    private String artist;
}
