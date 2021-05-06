package jpabook.start.test.entity;

import java.io.Serializable;

public class ChildId implements Serializable {
    private Long id;
    private MotherId motherId;

}
