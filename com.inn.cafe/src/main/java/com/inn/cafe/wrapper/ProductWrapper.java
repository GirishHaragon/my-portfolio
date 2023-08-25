package com.inn.cafe.wrapper;

import lombok.Data;

@Data
public class ProductWrapper {//In this class we will specify the columns or keys in which we want to return the values.

    //The keys we need
    Integer id;
    String name;
    String description;
    Integer price;
    String status;
    Integer categoryId;
    String categoryName;

    public ProductWrapper(){
    }

    public ProductWrapper(Integer id, String name, String description, Integer price, String status, Integer categoryId, String categoryName){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.status = status;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public ProductWrapper(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public ProductWrapper(Integer id, String name, String description, Integer price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

}
