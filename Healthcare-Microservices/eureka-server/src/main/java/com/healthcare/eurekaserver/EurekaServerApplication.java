package com.healthcare.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka Server Application
 * 
 * @EnableEurekaServer tells Spring Boot to configure this application as a Service Registry.
 * All other microservices (user-service, appointment-service, api-gateway) will register
 * their hostname and port with this Eureka Server.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
