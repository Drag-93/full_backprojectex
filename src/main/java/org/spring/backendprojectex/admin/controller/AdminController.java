package org.spring.backendprojectex.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.spring.backendprojectex.community.service.CommunityService;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private final MemberService memberService;
    private final ItemService itemService;
    private final CommunityService communityService;

    @Value("${kakao.map.appkey}")
    private String kakaoMapKey;

    @GetMapping({"/index","", "/", "/member"})
    public String member(@PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam(name = "subject", required = false) String subject,
                         @RequestParam(name = "search", required = false) String search,
                         Model model) {
        Page<MemberDto> memberList = memberService.pagingSearchMemberList(pageable, subject, search);
        System.out.println(memberList);
        setPaging(model, memberList, 3); // 페이지 계산 공통 메서드 사용
        model.addAttribute("content", memberList);
        model.addAttribute("key", "member");
        return "admin/admin";
    }

    @GetMapping({"/itemList"})
    public String itemList(@PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                           @RequestParam(name = "subject", required = false) String subject,
                           @RequestParam(name = "search", required = false) String search,
                           Model model) {
        Page<ItemDto> itemList = itemService.pagingSearchItemList(pageable, subject, search);
        System.out.println(itemList);
        setPaging(model, itemList, 3); // 페이지 계산 공통 메서드 사용
        model.addAttribute("content", itemList);
        model.addAttribute("key", "itemList");
        return "admin/admin";
    }

    @GetMapping("/itemInsert")
    public String insert(Model model) {
        model.addAttribute("itemDto", new ItemDto());
        model.addAttribute("key", "itemInsert");
        return "admin/admin";
    }

    @PostMapping("/itemInsert")
    public String writeOk(@Valid ItemDto itemDto,
                          BindingResult bindingResult,
                          Model model) throws IOException{
        if(bindingResult.hasErrors()){
            model.addAttribute("key","itemInsert");
            return "admin/admin";
        }
        itemService.insertItem(itemDto);
        return "redirect:/admin/itemList";
    }



    @GetMapping("/itemDetail/{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model) {
        ItemDto itemDetail=itemService.itemDetailFn(id);
        model.addAttribute("item", itemDetail);
        model.addAttribute("key", "itemDetail");
        return "admin/admin";
    }

    @GetMapping("/weather")
    public String weather(Model model){
        System.out.println("카카오키="+kakaoMapKey);
        model.addAttribute("kakaoMapKey",kakaoMapKey);
        model.addAttribute("key","weather");
        return "admin/admin";
    }

    @GetMapping("/scene")
    public String scene(Model model){
        model.addAttribute("key","scene");
        return "admin/admin";
    }

    @GetMapping("/bus")
    public String bus(Model model){
        System.out.println("카카오키="+kakaoMapKey);
        model.addAttribute("kakaoMapKey",kakaoMapKey);
        model.addAttribute("key","bus");
        return "admin/admin";
    }
    @GetMapping("/chat")
    public String chat(Model model){
        model.addAttribute("key","chat");
        return "admin/admin";
    }
    @GetMapping("/community")
    public String community(@PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name = "subject", required = false) String subject,
                            @RequestParam(name = "search", required = false) String search,
                            Model model) {
        Page<CommunityDto> communityList = communityService.communityListFn(pageable, subject, search);
        System.out.println(communityList);
        setPaging(model, communityList, 3); // 페이지 계산 공통 메서드 사용
        model.addAttribute("content", communityList);
        model.addAttribute("key", "community");
        return "admin/admin";
    }

    @GetMapping("/communityDetail/{id}")
    public String communityDetail(@PathVariable("id") Long id,
                                  Model model) {
        CommunityDto communityDto = communityService.communityDetailFn(id);
        model.addAttribute("community", communityDto);
        model.addAttribute("key","communityDetail");
        return "admin/admin";
    }
    @GetMapping("/communityWrite")
    public String communityWrite(Model model){
        model.addAttribute("key","communityWrite");
        return "admin/admin";
    }
    @PostMapping("/communityWrite")
    public String communityWriteOk(@Valid CommunityDto communityDto,
                          BindingResult bindingResult,
                          Model model) throws IOException{
        if (bindingResult.hasErrors()) {
            return "admin/communityWrite";
        }
        communityService.communityInsert(communityDto);
        //회원가입 -> list이동
        return "redirect:/admin/community";
    }
    private void setPaging(Model model, Page<?> pageData, int blockSize) {
        System.out.println(pageData);
        int currentPage = pageData.getNumber();
        int totalPages = pageData.getTotalPages();
        int startPage = (int) ((Math.floor(currentPage / blockSize) * blockSize) + 1);
        if (startPage > totalPages) startPage = totalPages;

        int endPage = Math.min(startPage + blockSize - 1, totalPages);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
    }
}

