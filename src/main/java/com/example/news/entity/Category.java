package com.example.news.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private String name;// = CategoryType.business;


//    public enum CategoryType {
//        business,
//        entertainment,
//        health,
//        science,
//        sports,
//        technology
//    }

    public static Category fromName(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }
}
