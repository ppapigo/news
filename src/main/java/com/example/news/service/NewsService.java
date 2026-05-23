package com.example.news.service;

import com.example.news.dto.ArticleDTO;
import com.example.news.dto.ArticleResponse;
import com.example.news.dto.SourceDTO;
import com.example.news.dto.SourceResponse;
import com.example.news.entity.Article;
import com.example.news.entity.Category;
import com.example.news.entity.Source;
import com.example.news.repository.ArticleRepository;
import com.example.news.repository.CategoryRepository;
import com.example.news.repository.SourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final RestClient newsApiRestClient;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final SourceRepository sourceRepository;

    public ArticleResponse fetchTopHeadlines(String country, String category, String pageNum) {

        ArticleResponse response = newsApiRestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("country", country)
                        .queryParam("category", category)
                        .queryParam("page",pageNum)
                        .build()) // query parameter 를 포함하는 url을 생성함
                .retrieve()  //request를 호출
                .body(ArticleResponse.class);

        //total Results 출력
        // ArticleResponse::articles의 아이템의 개수 출력
        if(response!=null) {
            System.out.println("totalResults: "+response.getTotalResults());
            System.out.println("response.articles의 개수: "+response.getArticles().size());
        }
        return response;

    }
    @Transactional
    public IngestResult ingestTopHeadLines(String country, String categoryName, String page){
        ArticleResponse articleResponse = fetchTopHeadlines(country, categoryName,page);
        if(articleResponse==null) {
            System.out.println("fetchTopHeadLInes 호출이 실패함");
            return new IngestResult("fetchTopHeadLines 호출 실패", 0, 0, 0);
        }


        System.out.println(country + " " + categoryName);

        if(!articleResponse.getStatus().equalsIgnoreCase("ok") ||
            articleResponse.getArticles().isEmpty()){
            System.out.println("status: " + articleResponse.getStatus());
            System.out.println("status가 ok가 아니거나 articles가 비어있음");

            return new IngestResult("status가 ok가 아니거나 기사 없음", 0, 0, 0);
        }

        Category category = categoryInsertOrGet(categoryName);
        if(category == null ){
            return new IngestResult("category data를 생성하거나 가져올 수 없습니다",0,0,0);
        }
        List<ArticleDTO> articles = articleResponse.getArticles();
        int total =articles.size();
        int saved = 0;
        int skip = 0;
        for(ArticleDTO articleDTO : articles){
            try {
                if(articleDTO.getAuthor()==null || articleDTO.getAuthor().length() > 100){
                    skip++;
                    continue;
                }
                Source source = sourceInsertOrGet(articleDTO.getSource(), category);
                if (source == null) {
                    skip++;
                    continue;
                }

                if (articleRepository.existsByTitle(articleDTO.getTitle())) {
                    skip++;
                    continue;
                }

                Article article = Article.fromDTO(articleDTO, source, category);

                articleRepository.save(article);
                saved++;
            } catch (DataIntegrityViolationException e) {
                System.out.println("Database System Error: "+e.getMessage());
                skip++;
            }
        }
        return new IngestResult("Ingest 작업완료",total,saved,skip);
    }

    @Transactional
    public Category categoryInsertOrGet(String categoryName){
        if(categoryName == null || categoryName.isBlank())
            return null;

        //이미 categoryName의 category항목이 category Table 에 있으면 해당 Category Entity를 반환하고
        //없으면 Category 인스턴스를 새로 생성하여 저장하고 saved category Entity를 반환
        return categoryRepository.findByName(categoryName).orElseGet(
                ()->categoryRepository.save(Category.fromName(categoryName))
        );
    }

    @Transactional
    public Source sourceInsertOrGet(SourceDTO sourceDTO, Category category){
        if(sourceDTO == null || sourceDTO.getName().isBlank())
         return null;

        //이미 categoryName의 category항목이 category Table 에 있으면 해당 Category Entity를 반환하고
        //없으면 Category 인스턴스를 새로 생성하여 저장하고 saved category Entity를 반환
        return sourceRepository.findByName(sourceDTO.getName()).orElseGet(
                ()->sourceRepository.save(Source.fromDTO(sourceDTO,category))
        );
    }

    public SourceResponse fetchSource(String url){
        RestClient restClient = RestClient.create();

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(SourceResponse.class);
    }

    @Transactional
    public IngestResult ingestSource(String url){
        SourceResponse sourceResponse = fetchSource(url);
        return ingestSource(sourceResponse);
    }

    @Transactional
    public IngestResult ingestSource(SourceResponse sourceResponse){
        if(sourceResponse == null)
            return new IngestResult("SourceResponse 인스턴스가 null잊니다",0,0,0);

        List<SourceDTO> sources = sourceResponse.getSources();
        int skip=0;
        int saved=0;
        int total= sources.size();
        if(total ==0) {
            return new IngestResult("저장할 source 데이터가 없습니다", 0, 0, 0);
        }
        for(SourceDTO sourceDTO : sources){
            Category category = categoryInsertOrGet(sourceDTO.getCategory());
            if(category==null){
                skip++;
                continue;


            }
            if(sourceRepository.existsByName(sourceDTO.getName())) {
                skip++;
                continue;
            }
            Source source = Source.fromDTO(sourceDTO, category);
            sourceRepository.save(source);
            saved++;


            }


        return new IngestResult("Ingest 작업완료",total,saved,skip);
        }

    public record IngestResult(String message,int total,int saved, int skipped){

    }

}



    //ingest 결과 데이터

