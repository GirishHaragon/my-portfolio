package com.inn.cafe.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@NamedQuery(name = "Product.getAllProduct", query = "select new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price, p.status, p.category.id, p.category.name) from Product p")

@NamedQuery(name = "Product.updateProductStatus", query = "update Product p set p.status=:status where p.id=:id") //What we have written "p.status=:status" is just as equal as we wrote status='"+status+"' in JDBC Codings.

@NamedQuery(name = "Product.getProductByCategory", query = "select new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name) from Product p where p.category.id=:id and p.status='true'")

@NamedQuery(name = "Product.getProductById", query = "select new com.inn.cafe.wrapper.ProductWrapper(p.id, p.name, p.description, p.price) from Product p where p.id=:id")

@Data//This anno provides 0Arg constructor for our application.
@Entity
@DynamicInsert//In CRUD operation video he has already explained what are these..
@DynamicUpdate
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 123456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    //Another variable is needed is Category which has to be joined with product
    //Each category has to be associated with a category
    @ManyToOne(fetch = FetchType.LAZY)//Lazy means if we fire the query to select all then it will not select the all parts of category. It acts Lazy
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Integer price;

    @Column(name = "status")
    private String status;

}
