package com.example.filekeep.reponses;


import com.example.filekeep.enums.Status;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiSuccessResponse<T>{
    private final T data;
    private final String message;
    private final Status status = Status.SUCCESS;
    private final String path;


    public ApiSuccessResponse(T data, String message,String path){
        this.data = data;
        this.message = message;
        this.path = path;
    }
}
