package com.healthcare.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * API Gateway Application
 * 
 * Acting as the Single Entry Point (Reverse Proxy) for all incoming client requests (e.g. React UI).
 * It communicates with Eureka Server to discover microservice instances dynamically,
 * and routes requests to them using client-side load balancing (lb://).
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
