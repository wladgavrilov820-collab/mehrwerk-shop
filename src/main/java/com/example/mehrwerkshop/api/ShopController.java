package com.example.mehrwerkshop.api;

import com.example.mehrwerkshop.dto.ShopDTO;
import com.example.mehrwerkshop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public Page<ShopDTO> getShops(Pageable pageable) {
        return shopService.getShops(pageable);
    }
}