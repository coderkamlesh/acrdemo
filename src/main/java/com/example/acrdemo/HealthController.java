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
            // 1. Create table if not exists (using CockroachDB INT8 unique_rowid)
            jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS app_users (
                    id INT8 PRIMARY KEY DEFAULT unique_rowid(),
                    username VARCHAR(50) UNIQUE NOT NULL,
                    email VARCHAR(100) NOT NULL,
                    created_at TIMESTAMPTZ DEFAULT clock_timestamp()
                )
            """);

            // 2. Insert default test user if it doesn't already exist
            jdbcTemplate.update("""
                INSERT INTO app_users (username, email)
                VALUES ('kamlesh', 'kamlesh@example.com')
                ON CONFLICT (username) DO NOTHING
            """);

            // 3. Query records to verify read functionality
            List<Map<String, Object>> users = jdbcTemplate.queryForList(
                    "SELECT id, username, email, created_at FROM app_users LIMIT 5"
            );

            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "running",
                    "database", "cockroachdb-connected",
                    "data", users
            ));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(Map.of(
                    "status", false,
                    "message", "Database operation failed",
                    "error", ex.getMessage()
            ));
        }
    }
}