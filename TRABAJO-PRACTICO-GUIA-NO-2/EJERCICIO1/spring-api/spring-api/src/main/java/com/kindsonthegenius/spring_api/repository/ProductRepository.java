package com.kindsonthegenius.spring_api.repository;

import com.kindsonthegenius.spring_api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
