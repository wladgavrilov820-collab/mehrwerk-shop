package com.example.mehrwerkshop.service.impl;

import com.example.mehrwerkshop.client.ShopClient;
import com.example.mehrwerkshop.dto.ShopDTO;
import com.example.mehrwerkshop.dto.ShopPageResponseDTO;
import com.example.mehrwerkshop.service.ImportService;
import com.example.mehrwerkshop.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImportServiceImpl implements ImportService {

    private final ShopClient shopClient;
    private final ShopService shopService;

    @Override
    public void importShops() {
        int page = 1;

        ShopPageResponseDTO response = shopClient.fetchShops(page);

        List<ShopDTO> allShops = new ArrayList<>(response.items());

        int totalPages = response.numberOfPages();

        for (page = 2; page <= totalPages; page++) {
            response = shopClient.fetchShops(page);
            allShops.addAll(response.items());
        }

        shopService.saveAllShops(allShops);
    }
}
