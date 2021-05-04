package jpabook.start.test.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class Child {
    @EmbeddedId
    private ChildId id;

    @MapsId
    @ManyToOne
    private Mother mother;

}
