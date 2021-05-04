package jpabook.start.test.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(ParentId.class)
public class Parent {
    @Id
    private String id1;

    @Id
    private String id2;

}
