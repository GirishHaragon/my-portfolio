package com.inn.cafe.dao;

import com.inn.cafe.POJO.Product;
import com.inn.cafe.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductDao extends JpaRepository<Product, Integer> {

    List<ProductWrapper> getAllProduct();

    @Modifying//If we don't use this anno, it will not modify the record.
    @Transactional
    Integer updateProductStatus(@Param("status") String status, @Param("id") int id);

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    ProductWrapper getProductById(@Param("id") Integer id);
}
