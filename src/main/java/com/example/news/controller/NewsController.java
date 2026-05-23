package com.example.news.controller;

import com.example.news.config.NewsApiProperties;
import com.example.news.dto.*;
import com.example.news.entity.Category;
import com.example.news.service.ArticleService;
import com.example.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//Mvc: 통합
//Model: 데이터를 다루는 객체
//View: 데이터를 보여주는 객체
//Controller: 호출을 제어하는 객체

//MVP: 분리
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class NewsController {
    private final NewsService newsService;
    private final ArticleService articleService;
    private final NewsApiProperties newsApiProperties;

    /*@GetMapping("/top-headLines")
    public ArticleResponse topHeadLines(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue="1")String page
    ){
        String countryName = country==null ? newsApiProperties.country() : country;
        String categoryName = category==null ? newsApiProperties.defaultCategory():category;


        return newsService.fetchTopHeadlines(countryName,categoryName,page);
    }*/
    @PostMapping(value="/sync",produces= MediaType.APPLICATION_JSON_VALUE)
    public NewsService.IngestResult ingestTopHeadLines(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue="1")String page
    ){
        String countryName = country==null ? newsApiProperties.country() : country;
        String categoryName = category==null ? newsApiProperties.defaultCategory():category;


        return newsService.ingestTopHeadLines(countryName, categoryName,page);
    }
    @GetMapping("/source")
    public SourceResponse fetchSource() {

        String url =String.format("%s?apiKey=%s",
                newsApiProperties.sourceUrl(),
                newsApiProperties.apiKey()
                );

        return newsService.fetchSource(url);

    }

    @PostMapping("/sources")
    public NewsService.IngestResult ingestSource() {
        String url =String.format("%s?apiKey=%s",
                newsApiProperties.sourceUrl(),
                newsApiProperties.apiKey()
        );
        System.out.println("sourceUrl = " + newsApiProperties.sourceUrl());
        System.out.println("apiKey = " + newsApiProperties.apiKey());
        return newsService.ingestSource(url);
    }

    @GetMapping("/health")
    public String health(){
        return "News Service Running...";
    }

    @GetMapping("/")
    public ArticleResponse news(
            @RequestParam(required = false, name="category"/*defaultValue = "business"*/) String categoryName,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false, defaultValue = "1") Integer page
    ){
        Category category = null;
        if(categoryName != null) {
            category = articleService.getCategory(categoryName);
        }
        List<ArticleDTO> articles = articleService.getArticles(category, pageSize, page);

        ArticleResponse response =new ArticleResponse();
        response.setStatus("ok");
        response.setTotalResults((long) articles.size());
        response.setPage((long) page);
        response.setArticles(articles);

        return response;

    }
    @GetMapping("/categories")
    public CategoryResponse getCategories(){
        List<CategoryDTO> categories = articleService.getCategories();
        CategoryResponse response = new CategoryResponse();
        response.setResults((long)categories.size());
        response.setCategories( categories );

        return response;
    }

    @GetMapping("/search")
    public ArticlePageResponse search(
            @RequestParam(name = "q")String q,
            @RequestParam(required = false, name="category") String categoryName,
            @RequestParam(required = false, defaultValue="20")Integer pageSize,
            @RequestParam(required = false, defaultValue="1")Integer page

    ){
        Page<ArticleDTO> articles = articleService.search(q,categoryName,pageSize,page);

        ArticlePageResponse response = new ArticlePageResponse();
        response.setStatus("ok");
        response.setArticles(articles);

        return response;
    }

}
