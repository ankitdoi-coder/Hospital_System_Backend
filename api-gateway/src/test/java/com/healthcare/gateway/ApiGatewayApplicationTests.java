package com.healthcare.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false"
})
class ApiGatewayApplicationTests {

    @Test
    @DisplayName("Verify API Gateway context loads successfully")
    void contextLoads() {
    }
}
