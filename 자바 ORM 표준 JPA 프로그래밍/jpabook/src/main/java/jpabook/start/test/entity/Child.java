package jpabook.start.test.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class Child {
    @EmbeddedId
    private ChildId id;

    @ManyToOne
    @MapsId("motherId") // MapsId를 하지 않으면 단순한 비식별 다대일 관계로 매핑됨
    private Mother mother;

    private String name;
}
