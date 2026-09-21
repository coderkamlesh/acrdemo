package com.example.acrdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HealthController {

    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public String health() {
        // AWS Lambda Web Adapter readiness probe check
        return "ok";
    }

   @GetMapping("/")
public ResponseEntity<?> index() {
    try {
        List<Map<String, Object>> users = jdbcTemplate.queryForList(
            "SELECT id, username, email, created_at FROM app_users LIMIT 5"
        );

        return ResponseEntity.ok(Map.of(
            "status", true,
            "database", "cockroachdb-connected",
            "data", users
        ));
    } catch (Exception ex) {
        return ResponseEntity.status(500).body(Map.of(
            "status", false,
            "error", ex.getMessage()
        ));
    }
}
}
