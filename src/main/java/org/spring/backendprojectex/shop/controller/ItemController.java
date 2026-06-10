package org.spring.backendprojectex.shop.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/shop")
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/insert") //보이기, DB데이터를 get
    public String insert(Model model) {
        model.addAttribute("itemDto", new ItemDto());
        return "shop/itemInsert";
    }

    @PostMapping("/insert") // DB추가
    public String itemInsertOk(@Valid ItemDto itemDto, BindingResult bindingResult) throws IOException {
        //설정된 유효성검사를 통과 못하면
        if (bindingResult.hasErrors()) {
            return "shop/itemInsert";
        }
        itemService.insertItem(itemDto);
        //회원가입 -> itemList이동
        return "redirect:/shop/itemList";
    }

//    @GetMapping({"/index", "/", "/itemList"})
//    public String itemList(Model model) {
//
//        List<ItemDto> itemList = itemService.itemListFn();
//
//        model.addAttribute("itemList", itemList);
//
//        return "shop/itemList";
//
//    }

    //paging
    @GetMapping({"/index", "/", "/itemList"})
    public String pagingList(@PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(name = "subject", required = false) String subject,
                             @RequestParam(name = "search", required = false) String search,
                             Model model) {
        Page<ItemDto> itemList = itemService.pagingSearchItemList(pageable, subject, search);

        int totalPages = itemList.getTotalPages();
        int newPage = itemList.getNumber();
        int blockNum = 3;

        int startPage = (int) ((Math.floor(newPage / blockNum) * blockNum) + 1 <= totalPages
                ? (Math.floor(newPage / blockNum) * blockNum) + 1
                : totalPages);
        int endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("newPage",newPage);

        model.addAttribute("itemList", itemList);

        return "shop/itemList";
    }


    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model) {
        ItemDto itemDto = itemService.oneItemFn(id);
        model.addAttribute("item", itemDto);
        return "shop/itemDetail";
    }
}
