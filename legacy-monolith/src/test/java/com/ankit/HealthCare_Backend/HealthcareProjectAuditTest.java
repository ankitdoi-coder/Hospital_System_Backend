package com.ankit.HealthCare_Backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Architectural and Deployment Audit Unit Test.
 * 
 * Answers:
 * 1. Are there real users or demo traffic?
 * 2. What did setup take before Docker Compose?
 * 3. Is "one command" (docker-compose up) strictly accurate?
 */
public class HealthcareProjectAuditTest {

    private final Path projectRoot = Paths.get("").toAbsolutePath();
    private final Path backendRoot = Files.exists(projectRoot.resolve("pom.xml")) 
            ? projectRoot 
            : projectRoot.resolve("HealthCare-Backend");

    @Test
    @DisplayName("Question 1: Verify system has NO real users and uses demo/synthetic data")
    void testUserDataAndTrafficCharacteristics() throws IOException {
        Path dataSqlPath = backendRoot.resolve("src/main/resources/data.sql");
        assertThat(Files.exists(dataSqlPath))
                .as("Seed file data.sql should exist in resources")
                .isTrue();

        List<String> sqlLines = Files.readAllLines(dataSqlPath);

        // Filter out empty lines and comments
        List<String> activeSqlStatements = sqlLines.stream()
                .map(String::trim)
                .filter(line -> !line.isEmpty() && !line.startsWith("--"))
                .toList();

        // 1. Confirm that only baseline role seeds exist, not real patient or clinical user records
        assertThat(activeSqlStatements)
                .as("data.sql must only contain role seeds (PATIENT, DOCTOR), zero pre-seeded real users")
                .allMatch(stmt -> stmt.startsWith("INSERT INTO roles"));

        long userInsertCount = activeSqlStatements.stream()
                .filter(stmt -> stmt.toLowerCase().contains("insert into users") || stmt.toLowerCase().contains("insert into patient"))
                .count();

        assertThat(userInsertCount)
                .as("There should be 0 pre-seeded real users or patients in the repository")
                .isEqualTo(0);

        // 2. Deployment status audit: Confirm the project is categorized as demo/portfolio readiness
        Path deploymentDoc = backendRoot.resolve("DEPLOYMENT.md");
        assertThat(Files.exists(deploymentDoc)).isTrue();
        String deploymentContent = Files.readString(deploymentDoc);

        assertThat(deploymentContent)
                .as("DEPLOYMENT.md confirms production setup (RDS, SSL, SMTP) is a pending checklist, not active live clinical prod")
                .contains("MOSTLY DEPLOYMENT READY")
                .contains("[ ] Cloud MySQL database provisioned")
                .contains("[ ] SSL certificate obtained");
    }

    @Test
    @DisplayName("Question 2: Verify pre-Docker Compose setup required multiple manual prerequisites")
    void testPreDockerComposeManualSetupRequirements() throws IOException {
        Path readmePath = backendRoot.resolve("README.md");
        assertThat(Files.exists(readmePath)).isTrue();
        String readmeContent = Files.readString(readmePath);

        // Verify pre-Docker manual setup required installing external dependencies
        assertThat(readmeContent)
                .as("Pre-Docker setup required manual local MySQL installation & database creation")
                .contains("CREATE DATABASE healthcaredb;")
                .contains("MySQL 8.x");

        assertThat(readmeContent)
                .as("Pre-Docker setup required manual local Redis installation & ping verification")
                .contains("redis-cli ping")
                .contains("Redis 7.x");

        assertThat(readmeContent)
                .as("Pre-Docker setup required manual JDK and Maven installs")
                .contains("Java 17+")
                .contains("Maven 3.x");

        // Verify microservices orchestrator script requires multi-process staggered startup
        Path microservicesScript = backendRoot.resolve("Healthcare-Microservices/start-microservices.ps1");
        if (Files.exists(microservicesScript)) {
            String scriptContent = Files.readString(microservicesScript);
            assertThat(scriptContent)
                    .as("Pre-Docker microservices launch required manual sleep delays across multiple terminals")
                    .contains("Start-Sleep -Seconds 15")
                    .contains("Start-Sleep -Seconds 10")
                    .contains("eureka-server")
                    .contains("api-gateway")
                    .contains("user-service")
                    .contains("appointment-service");
        }
    }

    @Test
    @DisplayName("Question 3: Verify whether 'one command' is strictly accurate or an oversimplification")
    void testDockerComposeOneCommandAccuracy() throws IOException {
        Path dockerComposePath = backendRoot.resolve("docker-compose.yml");
        assertThat(Files.exists(dockerComposePath))
                .as("docker-compose.yml should be present")
                .isTrue();

        String dockerComposeContent = Files.readString(dockerComposePath);

        // Point 1: Backend depends on a separate, manually provisioned .env file
        assertThat(dockerComposeContent)
                .as("Backend container requires an external .env file; 'one command' fails without pre-configuring secrets")
                .contains("env_file:")
                .contains("- .env");

        // Point 2: Cold-start race condition required custom start_period and healthcheck retry configuration
        assertThat(dockerComposeContent)
                .as("Cold-start initialization race condition on fresh volumes necessitated extended healthcheck start_period")
                .contains("start_period: 90s")
                .contains("retries: 10");

        // Point 3: Scope limitation - docker-compose only launches backend + MySQL + Redis; does not run microservices or frontend
        assertThat(dockerComposeContent)
                .as("Compose only orchestrates monolithic backend, mysql, and redis; frontend & microservices are separate")
                .contains("container_name: hospital_backend_app")
                .contains("container_name: hospital_db")
                .contains("container_name: hospital_redis");

        assertThat(dockerComposeContent)
                .as("Compose does not include React frontend or Eureka microservices cluster")
                .doesNotContain("eureka-server")
                .doesNotContain("frontend");
    }
}
