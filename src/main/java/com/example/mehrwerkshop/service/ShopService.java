package com.example.mehrwerkshop.service;

import com.example.mehrwerkshop.dto.ShopDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ShopService {

    Page<ShopDTO> getShops(Pageable pageable);

    void saveAllShops(List<ShopDTO> shopPageResponseDTO);
}
