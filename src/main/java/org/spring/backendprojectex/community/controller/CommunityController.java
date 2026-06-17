package org.spring.backendprojectex.community.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.community.dto.CommunityDto;
import org.spring.backendprojectex.community.service.CommunityService;
import org.spring.backendprojectex.shop.dto.ItemDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping({"","/","/index","communityList"})
    public String pagingList(@PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             @RequestParam(name = "subject", required = false) String subject,
                             @RequestParam(name = "search", required = false) String search,
                             Model model) {
        Page<CommunityDto> communityList = communityService.communityListFn(pageable, subject, search);

        int totalPages = communityList.getTotalPages();
        int newPage = communityList.getNumber();
        int blockNum = 3;

        int startPage = (int) ((Math.floor(newPage / blockNum) * blockNum) + 1 <= totalPages
                ? (Math.floor(newPage / blockNum) * blockNum) + 1
                : totalPages);
        int endPage = (startPage + blockNum) - 1 < totalPages ? (startPage + blockNum) - 1 : totalPages;

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("newPage",newPage);

        model.addAttribute("communityList", communityList);
        return "community/index";
    }
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id,
                         Model model) {
        CommunityDto communityDto = communityService.communityDetailFn(id);
        model.addAttribute("community", communityDto);
        return "community/communityDetail";
    }
    @GetMapping("/write")
    public String communityWrite(Model model){
        model.addAttribute("communityDto",new CommunityDto());
        return "community/communityWrite";
    }

    @PostMapping("/write") // DB추가
    public String communityWriteOK(@Valid CommunityDto communityDto, BindingResult bindingResult) throws IOException {
        //설정된 유효성검사를 통과 못하면
        if (bindingResult.hasErrors()) {
            return "community/communityWrite";
        }
        communityService.communityInsert(communityDto);
        //회원가입 -> list이동
        return "redirect:/community/index";
    }
}
