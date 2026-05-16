package com.example.news.repository;

import com.example.news.entity.Article;
import com.example.news.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Boolean existsByTitle(String title);
    
    List<Article> findByCategory(Category category);
}
