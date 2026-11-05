package com.example.mehrwerkshop.client;

import com.example.mehrwerkshop.dto.TokenResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenClient {

    private final RestClient restClient = RestClient.create();
    private final MehrwerkApiProperties properties;


    public TokenResponseDTO fetchAuthToken() {
        return restClient.post()
                .uri(properties.baseUrl() + "/v2/iam/oauth/token")
                .header("X-API-KEY", properties.key())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "client_id", properties.clientId(),
                        "client_secret", properties.clientSecret(),
                        "grant_type", "client_credentials"
                ))
                .retrieve()
                .body(TokenResponseDTO.class);
    }

}
