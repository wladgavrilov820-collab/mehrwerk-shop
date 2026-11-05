package com.example.mehrwerkshop.api;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopControllerITest {

    @Container
    @ServiceConnection
    static MSSQLServerContainer<?> sqlServer =
            new MSSQLServerContainer<>(
                    "mcr.microsoft.com/mssql/server:2022-latest"
            )
                    .acceptLicense();

    @Value("${local.server.port}")
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getShops_returnShops_200() throws Exception {
        RestClient client = RestClient.create(
                "http://localhost:" + port
        );

        String response = client.get()
                .uri("/api/shops?page=0&size=10")
                .retrieve()
                .body(String.class);

        assertThat(response).isNotNull();

        JsonNode json = objectMapper.readTree(response);

        assertThat(json.get("content").isArray()).isTrue();
        assertThat(json.get("content").size()).isEqualTo(10);
        assertThat(json.get("number").asInt()).isZero();
        assertThat(json.get("size").asInt()).isEqualTo(10);
        assertThat(json.get("totalElements").asLong())
                .isGreaterThan(0);

        JsonNode firstShop = json.get("content").get(0);

        assertThat(firstShop.get("id").asText()).isNotBlank();
        assertThat(firstShop.has("name")).isTrue();
        assertThat(firstShop.get("categories").isArray()).isTrue();
    }
}