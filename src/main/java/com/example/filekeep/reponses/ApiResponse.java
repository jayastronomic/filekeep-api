package com.example.filekeep.reponses;


import com.example.filekeep.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiResponse<T>{
    private final T data;
    private final String message;
    private final Status status;
    private final String path;
}
