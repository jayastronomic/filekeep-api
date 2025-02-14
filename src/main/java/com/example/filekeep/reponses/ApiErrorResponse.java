package com.example.filekeep.reponses;

import java.util.List;

import com.example.filekeep.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class ApiErrorResponse {
    private final List<String> errors;
    private final String message; 
    private final Status status = Status.ERROR;
}
