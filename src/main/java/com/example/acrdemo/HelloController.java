package com.example.acrdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    ResponseEntity<?> hello(){
        return ResponseEntity.ok(Map.of("message", "hello from testing"));
    }
}
