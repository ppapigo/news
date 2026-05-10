package com.example.news.repository;

import com.example.news.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByName(Category name);
    Optional<Category> findByName(String name);
}
