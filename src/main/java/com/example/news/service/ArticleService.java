package com.example.news.service;

import com.example.news.dto.ArticleDTO;
import com.example.news.dto.CategoryDTO;
import com.example.news.entity.Article;
import com.example.news.entity.Category;
import com.example.news.repository.ArticleRepository;
import com.example.news.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticles(Category category){
        if(category == null) {
            System.out.println("category == null");
            return articleRepository.findAll().stream().map(Article::toDTO).toList();
        }
        return articleRepository.findByCategory(category).stream().map(Article::toDTO).toList();

    }

    @Transactional(readOnly = true)
    public Category getCategory(String categoryName){
        System.out.println("ArticleService::getCategory: categoryName= "+categoryName);
       return categoryRepository.findByName(categoryName).orElseThrow();
    }

    public List<CategoryDTO> getCategories(){
        return categoryRepository.findAll().stream().map(Category::toDTO).toList();
    }
}
