package com.example.news.controller;

import com.example.news.config.NewsApiProperties;
import com.example.news.dto.ArticleResponse;
import com.example.news.dto.SourceResponse;
import com.example.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


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
    private final NewsApiProperties newsApiProperties;

    @GetMapping("/top-headLines")
    public ArticleResponse topHeadLines(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category
    ){
        String countryName = country==null ? newsApiProperties.country() : country;
        String categoryName = category==null ? newsApiProperties.defaultCategory():category;


        return newsService.fetchTopHeadlines(countryName,categoryName);
    }
    @PostMapping(value="/sync",produces= MediaType.APPLICATION_JSON_VALUE)
    public NewsService.IngestResult ingestTopHeadLines(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category
    ){
        String countryName = country==null ? newsApiProperties.country() : country;
        String categoryName = category==null ? newsApiProperties.defaultCategory():category;


        return newsService.ingestTopHeadLines(countryName, categoryName);
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
}
