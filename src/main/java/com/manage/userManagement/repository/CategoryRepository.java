package com.manage.userManagement.repository;

import com.manage.userManagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findById(int id);
    Optional<Category> findBySlug(String slug);
    Optional<Category> findByName(String name);
}
