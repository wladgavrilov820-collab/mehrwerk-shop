package com.example.mehrwerkshop.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mehrwerk.api")
public record MehrwerkApiProperties(
        String baseUrl,
        String key,
        String clientId,
        String clientSecret
) {
}
