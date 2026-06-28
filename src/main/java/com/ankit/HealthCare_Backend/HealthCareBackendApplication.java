package com.ankit.HealthCare_Backend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ankit.HealthCare_Backend.core.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class HealthCareBackendApplication {

	public static void main(String[] args) {
		// Load .env file if it exists (local dev), skip silently in production
		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(e -> {
			// Only set if not already set as a real system env var (production safe)
			if (System.getenv(e.getKey()) == null) {
				System.setProperty(e.getKey(), e.getValue());
			}
		});
		SpringApplication.run(HealthCareBackendApplication.class, args);
	}
}