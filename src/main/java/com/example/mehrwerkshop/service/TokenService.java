package com.example.mehrwerkshop.service;

import lombok.NonNull;


public interface TokenService {
    @NonNull
    String getAccessToken();
}
