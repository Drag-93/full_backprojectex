package org.spring.backendprojectex.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/insert") //보이기, DB데이터를 get
    public String insert(Model model){
        model.addAttribute("itemDto", new ItemDto());
        return "shop/itemInsert";
    }
    @PostMapping("/insert") // DB추가
    public String itemInsertOk(@Valid ItemDto itemDto, BindingResult bindingResult) throws IOException{
        //설정된 유효성검사를 통과 못하면
        if(bindingResult.hasErrors()){
            return "shop/itemInsert";
        }
        itemService.insertItem(itemDto);
        //회원가입 -> itemList이동
        return "redirect:/shop/itemList";
    }
    @GetMapping("/itemList")
    public String itemList(Model model){
        List<ItemDto> itemList=itemService.itemListFn();

        model.addAttribute("itemList",itemList);

        return "shop/itemList";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id")Long id, Model model){
        ItemDto itemDto=itemService.itemDetailFn(id);
        model.addAttribute("item",itemDto);
        return "shop/itemDetail";
    }
}
