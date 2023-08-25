package com.inn.cafe.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Category.getAllCategory", query = "select c from Category c where c.id in (select p.category from Product p where p.status='true')")//This query is just fetching all the records, later we will modify this query. //In the sub query we are extracting all the categories ids exist in the product p where the product status is true. Suppose we have 1 product so that status is true and it will extract the id for that one. The whole sub-query will return the category id (1)

@Data//This anno provides 0Arg constructor for our application.
@Entity
@DynamicInsert//In CRUD operation video he has already explained what are these..
@DynamicUpdate
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}
