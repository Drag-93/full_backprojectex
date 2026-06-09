package org.spring.backendprojectex.shop.service.impl;

import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
@Service
public class ItemServiceImpl implements ItemService {
    @Override
    public List<ItemDto> itemListFn() {
        return List.of();
    }

    @Override
    public Page<ItemDto> pagingSearchItemList(Pageable pageable, String subject, String search) {
        return null;
    }

    @Override
    public void insertItem(ItemDto itemDto) throws IOException {

    }

    @Override
    public ItemDto itemDetailFn(Long id) {
        return null;
    }
}
