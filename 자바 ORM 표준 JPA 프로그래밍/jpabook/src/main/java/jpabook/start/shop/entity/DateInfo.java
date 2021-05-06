package jpabook.start.shop.entity;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class DateInfo {

    Date registeredDate;
    Date modifiedDate;

}

