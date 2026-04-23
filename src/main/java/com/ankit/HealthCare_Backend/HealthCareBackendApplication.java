package com.ankit.HealthCare_Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ankit.HealthCare_Backend.core.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class HealthCareBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthCareBackendApplication.class, args);
	}
}
	