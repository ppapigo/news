package com.example.news.config;

import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@ConfigurationProperties(prefix = "news-api")
public record NewsApiProperties (

    @NotBlank String baseUrl,
    @NotBlank String apiKey,
    @NotBlank String sourceUrl,
    String country,
    String defaultCategory

){
    public NewsApiProperties{
        log.info("NewsApiProperties 인스턴스가 생성됨");
        log.info("baseUrl: {}",baseUrl);
        log.info("sourceUrl: {} ",sourceUrl);
        log.info("apiKey: {}", apiKey);
        System.out.println("NewsApiProperties 인스턴스가 생성됨");
        System.out.println("baseUrl: "+baseUrl);
        System.out.println("sourceUrl: "+sourceUrl);
        System.out.println("apiKey: "+ apiKey);
        System.out.println("country: "+country);
        System.out.println("defaultCategory: "+defaultCategory);
    }

}
