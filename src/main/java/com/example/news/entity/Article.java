package com.example.news.entity;

import com.example.news.dto.ArticleDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDateTime;

@Entity
@Table(name="articles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="author", length = 500)
    private String author;

    @Column(name="title", nullable = false, length = 500)
    private String title;

    @Column(name="description", length = 1000)
    private String description;

    @Column(name="url", nullable = false, length = 2000)
    private String url;

    @Column(name="url_to_image", length = 2000)
    private String urlToImage;

    @Column(name="published_at", nullable = false, length = 200)
    private String publishedAt;

    @Column(name="content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id",foreignKey = @ForeignKey(name ="articles_ibfk_1"))
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="source_id",foreignKey = @ForeignKey(name ="articles_ibfk_2"))
    private Source source;

    @Column(name="created_at", length = 200, updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    public static Article fromDTO(ArticleDTO dto,Source source,Category category){
        Article article=new Article();
        article.setAuthor(dto.getAuthor());
        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setUrl(dto.getUrl());
        article.setUrlToImage(dto.getUrlToImage());
        article.setPublishedAt(dto.getPublishedAt());
        article.setContent(dto.getContent());
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        article.setSource(source);
        article.setCategory(category);

        return article;
    }

    public static ArticleDTO toDTO(Article article){
        ArticleDTO dto = new ArticleDTO();
        dto.setAuthor(article.getAuthor());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setUrl(article.getUrl());
        dto.setUrlToImage(article.getUrlToImage());
        dto.setPublishedAt(article.getPublishedAt());
        dto.setContent(article.getContent());

        dto.setSource(Source.toDTO(article.getSource()));

        return dto;
        }
}
