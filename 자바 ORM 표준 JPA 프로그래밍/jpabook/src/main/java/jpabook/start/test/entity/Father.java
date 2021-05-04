package jpabook.start.test.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Father {

    @EmbeddedId
    private ParentId id;
}
