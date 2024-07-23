package com.manage.userManagement.repository;

import com.manage.userManagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlug(String slug);

    void deleteBySlug(String slug);
}
