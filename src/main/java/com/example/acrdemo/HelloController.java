package com.example.acrdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping
    ResponseEntity<?> hello(){
        return ResponseEntity.ok("hello from testing");
    }
}
