package org.spring.backendprojectex.shop.service;

import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.dto.ItemListDto;

import java.util.List;

public interface CartService {
    void insertCart(ItemDto itemDto);

    List<ItemListDto> cartList(Long memberId);

    int countCartItems(Long memberId);
}
