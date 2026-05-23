package com.example.news.repository;

import com.example.news.entity.Article;
import com.example.news.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Boolean existsByTitle(String title);
    
    List<Article> findByCategoryOrderByPublishedAtDesc(Category category, Pageable pageable);

    // 완전일치
    //List<Article> findByTitle(String title);

    //포함
    Page<Article> findByTitleContainingOrderByPublishedAtDesc(String title, Pageable pageable);

    //카테고리별 키워드 포함
    @Query("""
            SELECT a FROM Article a
            WHERE (:category = a.category) AND
            (a.title LIKE CONCAT('%', :title, '%'))
           ORDER BY a.publishedAt DESC
           """)
    Page<Article> searchInCategoryByTitleContaining(Category category,String title, Pageable pageable);

    @Query("""
            SELECT a FROM Article a
            WHERE (:category = a.category) AND
                  ((a.title LIKE CONCAT('%', :keyword, '%'))OR
                  (a.description LIKE CONCAT('%', :keyword, '%')))
                  ORDER BY a.publishedAt DESC
           """)
    Page<Article> searchInCategory(Category category, String keyword, Pageable pageable);
}
