package com.example.mehrwerkshop.client;

import com.example.mehrwerkshop.dto.ShopPageResponseDTO;
import com.example.mehrwerkshop.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopClient {

    private final RestClient restClient = RestClient.create();
    private final TokenService tokenService;
    private final MehrwerkApiProperties properties;

    public ShopPageResponseDTO fetchShops(int page) {
        return restClient.get()
                .uri(properties.baseUrl() + "/v3/cashback/shops?page={page}", page)
                .header("X-API-KEY", properties.key())
                .headers(headers -> headers.setBearerAuth(
                        tokenService.getAccessToken()
                ))
                .retrieve()
                .body(ShopPageResponseDTO.class);
    }
}

