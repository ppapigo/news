package com.example.news;

import com.example.news.config.NewsApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@SpringBootApplication
@ConfigurationPropertiesScan("com.example.news.config")
public class NewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsApplication.class, args);
	}

	@Bean
	RestClient newsApiRestClient(NewsApiProperties properties) {

		System.out.println("newsApiRestClient 가 호출 로" );

		return RestClient.builder()
				.baseUrl(properties.baseUrl())
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("X-Api-Key", properties.apiKey())
				.build();
	}
}