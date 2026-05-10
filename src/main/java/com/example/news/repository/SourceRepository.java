package com.example.news.repository;

import com.example.news.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Long> {
    Boolean existsByName(String name);

    Optional<Source> findByName(String name);
}
