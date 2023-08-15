package com.luv2code.SpringBootEcommerce.dao;

import com.luv2code.SpringBootEcommerce.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product,Long> {
    //http://localhost:8081/api/products/search/findByCategoryId?id=2
    Page<Product>findByCategoryId(@Param("id") Long id, Pageable pageable);

    //http://localhost:8081/api/products/search/findByNameContaining?name=crash
    Page<Product>findByNameContaining(@Param("name")String name, Pageable pageable);

    @Query("Select p from Product p where lower(p.name) like lower(concat('%',:searchTerm,'%'))"+ " OR lower(p.description) like (concat('%',:searchTerm,'%'))")
    Page<Product>doMyFancyStaff(@Param("searchTerm") String searchTerm, Pageable pageable);

}
