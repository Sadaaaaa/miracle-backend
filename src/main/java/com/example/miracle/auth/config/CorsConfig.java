//package com.example.miracle.auth.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Value("${api.url}")
//    private String apiUrl;
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/")
//                .allowedOrigins("http://localhost:3000", "http://localhost:8090", "http://sadopwnz.duckdns.com", "http://192.168.88.26")
//                .allowedMethods("GET", "POST", "OPTIONS", "DELETE", "PUT")
//                .allowedHeaders("*")
//                .allowCredentials(true);
//    }
//}
