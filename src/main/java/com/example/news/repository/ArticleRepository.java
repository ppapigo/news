package com.example.news.repository;

import com.example.news.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Boolean existsByTitle(String title);
}
