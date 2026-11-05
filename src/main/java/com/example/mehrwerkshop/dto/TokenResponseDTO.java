package com.example.mehrwerkshop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponseDTO(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") Long expiresIn,
        @JsonProperty("token_type") String tokenType
) {
}
