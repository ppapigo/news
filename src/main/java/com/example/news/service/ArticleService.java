package com.example.news.service;

import com.example.news.dto.ArticleDTO;
import com.example.news.dto.CategoryDTO;
import com.example.news.entity.Article;
import com.example.news.entity.Category;
import com.example.news.repository.ArticleRepository;
import com.example.news.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> getArticles(Category category, Integer pageSize, Integer page){
        if(page <=0) {
            return null;
            //나중에 예외로 발생하기
        }
        Pageable pageable = PageRequest.of(page-1, pageSize);
        if(category == null) {
            System.out.println("category == null");

            return articleRepository.findAll(pageable).map(Article::toDTO);//.toList();
        }

        return articleRepository.findByCategoryOrderByPublishedAtDesc(category,pageable)
                .map(Article::toDTO);
             //   .stream()
             //   .map(Article::toDTO)
             //   .toList();

    }

    @Transactional(readOnly = true)
    public Category getCategory(String categoryName){
        //System.out.println("ArticleService::getCategory: categoryName= "+categoryName);
        log.debug("ArticleService::getCategory: categoryName= {}",categoryName);
       return categoryRepository.findByName(categoryName).orElseThrow();
    }

    public List<CategoryDTO> getCategories(){
        return categoryRepository.findAll().stream().map(Category::toDTO).toList();
    }

    public Page<ArticleDTO> search(String q, String categoryName, Integer pageSize, Integer page) {

        Pageable pageable = PageRequest.of(page-1,pageSize);

        if(categoryName !=null && !categoryName.isBlank()){
            Category category = categoryRepository.findByName(categoryName).orElseThrow();
            return articleRepository.searchInCategoryByTitleContaining(category,q,pageable).map(Article::toDTO);
        }

        Page<ArticleDTO> articles =articleRepository.findByTitleContainingOrderByPublishedAtDesc(q,pageable).map(Article::toDTO);

        return articles;
    }
}
