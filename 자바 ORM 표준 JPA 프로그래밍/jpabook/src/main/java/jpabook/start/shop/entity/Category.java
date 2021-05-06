package jpabook.start.shop.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Category extends DateInfo{

    @Id
    private long id;

    @ManyToMany(mappedBy = "categories")
    private List<Item> items;

}
