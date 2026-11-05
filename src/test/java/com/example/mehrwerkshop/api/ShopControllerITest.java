package com.example.mehrwerkshop.api;

import com.example.mehrwerkshop.dto.ShopDTO;
import com.example.mehrwerkshop.repository.ShopRepository;
import com.example.mehrwerkshop.service.ShopService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.MSSQLServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopControllerITest {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private CacheManager cacheManager;

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

    @Test
    void shouldCacheShopsAndEvictAfterImport() {
        String shopId = "000-cache-test-shop";

        shopService.saveAllShops(List.of(
                new ShopDTO(shopId, "Old Name", "Description", List.of())
        ));

        String url = "http://localhost:" + port
                + "/api/shops?page=0&size=10&sort=id,asc";

        Cache cache = cacheManager.getCache("shops");
        assertThat(cache).isNotNull();

        String firstResponse = RestClient.create()
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        assertThat(firstResponse).contains("Old Name");

        var shop = shopRepository.findById(shopId).orElseThrow();
        shop.setName("Changed Directly");
        shopRepository.saveAndFlush(shop);

        Pageable pageable = PageRequest.of(
                0,
                10,
                Sort.by("id").ascending()
        );

        System.out.println("=== CACHE BEFORE SECOND GET ===");

        System.out.println(
                "Cache value: " + cache.get(pageable).get()
        );

        System.out.println(
                "Cache keys: " +
                        ((com.github.benmanes.caffeine.cache.Cache<?, ?>)
                                cache.getNativeCache()).asMap().keySet()
        );

        String cachedResponse = RestClient.create()
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        assertThat(cachedResponse).contains("Old Name");
        assertThat(cachedResponse).doesNotContain("Changed Directly");


        shopService.saveAllShops(List.of(
                new ShopDTO(shopId, "Updated Name", "Description", List.of())
        ));


        String updatedResponse = RestClient.create()
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);

        assertThat(updatedResponse).contains("Updated Name");
    }
}