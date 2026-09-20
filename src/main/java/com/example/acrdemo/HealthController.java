package com.example.acrdemo;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/health")
    public String health() {
        return "ok";
    }

    @GetMapping("/")
    public ResponseEntity<?> index() {
        return ResponseEntity.ok(Map.of("status",true,"message","running"));
    }
}