package com.example.news.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private Integer status;
    private String error;
    private String message;
    private LocalDateTime occur;


    public static ErrorResponse of(int status,String error, String message){
        return new ErrorResponse(status, error, message, LocalDateTime.now());
    }
}
