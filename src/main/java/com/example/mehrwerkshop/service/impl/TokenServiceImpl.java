package com.example.mehrwerkshop.impl;

import com.example.mehrwerkshop.client.AuthTokenClient;
import com.example.mehrwerkshop.dto.TokenResponse;
import com.example.mehrwerkshop.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final AuthTokenClient authTokenClient;

    private String accessToken;

    private Instant expiresAt = Instant.EPOCH;

    @Override
    @NonNull
    public synchronized String getAccessToken() {
        if (accessToken == null || Instant.now().isAfter(expiresAt)) {
            TokenResponse response = authTokenClient.fetchAuthToken();

            if (response == null ||
                    response.accessToken() == null ||
                    response.accessToken().isBlank()) {
                throw new IllegalStateException(
                        "Authentication API returned no access token"
                );
            }

            accessToken = response.accessToken();

            expiresAt = Instant.now().plusSeconds(Math.max(0, response.expiresIn() - 30));
        }

        return accessToken;
    }
}
