package org.spring.backendprojectex.shop;

import org.junit.jupiter.api.Test;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ItemTest {
    @Autowired
    ItemService itemService;


    @Test
    void insert() throws IOException {
        for (int i = 1; i <= 10; i++) {
            ItemDto itemDto = ItemDto.builder()
                    .itemTitle("상품" + i)
                    .itemDetail("m" + i + "상세내용")
                    .itemPrice(10000)
                    .itemSize(5)
                    .memberId(1L)
                    .attachFile(0)
                    .build();
            itemService.insertItem(itemDto);
        }
    }
}