package com.example.mehrwerkshop.runner;

import com.example.mehrwerkshop.service.ImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShopImportRunner implements ApplicationRunner {

    private final ImportService importService;

    @Override
    public void run(ApplicationArguments args) {
        importService.importShops();
        log.info("all shops successfully imported to Database");
    }
}