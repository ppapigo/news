package com.example.news.entity;

import com.example.news.dto.SourceDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="source")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sid", nullable=false,length =250)
    private String sid;

    @Column(name="name",nullable = false)
    private String name;

    @Column(name="description", length = 1000)
    private String description;

    @Column(name = "url", length = 2000)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category",foreignKey = @ForeignKey(name ="fk_source_category"))
    private Category category;

    @Column(name = "language", length = 50)
    private String language;

    @Column(name = "country", length = 50)
    private String country;

    public static Source fromDTO(SourceDTO dto, Category category){
        Source source =new Source();
        source.setName(dto.getName());
        source.setSid(dto.getId());
        source.setCategory(category);
        source.setDescription(dto.getDescription());
        source.setUrl(dto.getUrl());
        source.setLanguage(dto.getLanguage());
        source.setCountry(dto.getCountry());

        return source;

    }



}
