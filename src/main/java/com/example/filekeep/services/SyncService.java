package com.example.filekeep.services;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.filekeep.dtos.SyncData;

@Service
public class SyncService extends ApplicationService {
    private final WebClient webClient;

    public SyncService(WebClient webClient) {
        this.webClient = webClient;
    }
 
    
    public SyncData sync(SyncData payload) {
        return webClient.post()
                .uri("/sync")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(SyncData.class)
                .block();
    }
}
