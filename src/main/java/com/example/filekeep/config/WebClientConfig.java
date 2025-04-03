// package com.example.filekeep.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.reactive.function.client.WebClient;

// @Configuration
// public class WebClientConfig {
    
//     @Value("${sync.api-url}")
//     private String syncApiUrl;

//     @Bean
//     public WebClient webClient(WebClient.Builder builder) {
//         return builder
//                 .baseUrl(syncApiUrl)
//                 .build();
//     }
// }
