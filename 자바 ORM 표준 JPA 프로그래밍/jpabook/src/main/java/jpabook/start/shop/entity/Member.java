package jpabook.start.shop.entity;

import javax.persistence.*;  //**
import java.util.Date;
import java.util.List;


/**
 * User: HolyEyE
 * Date: 13. 5. 24. Time: 오후 7:43
 */
@Entity
@Table(name="MEMBER", uniqueConstraints = {@UniqueConstraint(
        name = "NAME_AGE_UNIQUE",
        columnNames = {"NAME","AGE"}
        )})
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,allocationSize = 1
)
public class Member {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 10)
    private String username;

    private String city;
    private String street;
    private String zipcode;

    private Integer age;

    private RoleType roleType;

    private Date registerdDate;
    private Date lastodifiedDate;

    private String description;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    //    private MyCustomClassType customClassType;
    @OneToMany(mappedBy = "member2")
    private List<Order> orders;

    // Getter, Setter
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
}