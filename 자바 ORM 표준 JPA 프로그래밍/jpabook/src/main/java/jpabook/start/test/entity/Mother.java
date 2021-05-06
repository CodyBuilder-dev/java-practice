package jpabook.start.test.entity;

import javax.persistence.*;

@Entity
public class Mother {
    @EmbeddedId
    private MotherId id;

    @ManyToOne
    @MapsId("grandMotherId") // 기본키(MotherId)의 필드명에 매핑하여 식별관계로 만들겠다
    private GrandMother grandmother;

    private String name;
}
