package jpabook.start.test.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class GrandMother {
    @Id
    private Long id;

    private String name;
}
