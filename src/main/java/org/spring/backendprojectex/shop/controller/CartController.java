package org.spring.backendprojectex.shop.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/insert/addCart")
    public String addCart0(ItemDto itemDto){
        cartService.insertCart(itemDto);

        return "redirect:/cart/cartList/"+itemDto.getMemberId();//자신의 장바구니
    }

    @PostMapping("/insert/addCart2")
    @ResponseBody
    public String addCart2(@RequestBody ItemDto itemDto){
        cartService.insertCart(itemDto);
        return "1";
    }

    @GetMapping("/cartList/{memberId}")
    public String cartList(@PathVariable("memberId") Long memberId,
                           Model model){
        List<ItemListDto> itemListDtos=cartService.cartList(memberId);
        int total = itemListDtos.stream()
                .mapToInt(item ->
                        item.getItemEntity().getItemPrice() * item.getItemSize())
                .sum();

        model.addAttribute("itemList", itemListDtos);
        model.addAttribute("memberId", memberId);
        model.addAttribute("total", total);
        System.out.println(itemListDtos.size());
        return "shop/cartList";

    }






}
