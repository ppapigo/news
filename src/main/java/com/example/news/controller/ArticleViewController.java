package com.example.news.controller;

import com.example.news.dto.ArticleDTO;
import com.example.news.dto.CategoryDTO;
import com.example.news.entity.Category;
import com.example.news.service.ArticleService;
import com.example.news.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleViewController {

    private final ArticleService articleService;
    private final NewsService newsService;

    @GetMapping("/news")
    public String index(
            @RequestParam(name = "category",required = false)String categoryName,
            @RequestParam(required = false,defaultValue = "20")Integer pageSize,
            @RequestParam(required = false,defaultValue = "1")Integer page,
            Model model
    ){

        Category category =(categoryName == null) ? null: articleService.getCategory(categoryName);


        List<ArticleDTO> articles = articleService.getArticles(category,pageSize,page);
        List<CategoryDTO> categories=articleService.getCategories();

        System.out.println("가져온 뉴스 데이터개수:"+articles.size()+"개");

        model.addAttribute("title","뉴스서비스");
        model.addAttribute("articles",articles);
        model.addAttribute("categories",categories);
        model.addAttribute("selected",categoryName);
        //카테고리 목록 전송

        return "news";
    }
}
