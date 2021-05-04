package jpabook.start.test.entity;

import javax.persistence.*;

@Entity
public class Mother {
    @EmbeddedId
    private MotherId id;

    @ManyToOne
    @MapsId
    private GrandMother grandmother;

}
