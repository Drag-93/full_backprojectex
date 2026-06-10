package org.spring.backendprojectex.admin.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.spring.backendprojectex.shop.service.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping({"/index", "/", "/member"})
    public String member(@PageableDefault(page = 0, size = 8, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam(name = "subject", required = false) String subject,
                         @RequestParam(name = "search", required = false) String search,
                         Model model) {
        Page<MemberDto> memberList = memberService.pagingSearchMemberList(pageable, subject, search);
        setPaging(model,memberList,3); // 페이지 계산 공통 메서드 사용
        model.addAttribute("memberList", memberList);
        model.addAttribute("key","member");
        return "admin/admin";
    }

    private void setPaging(Model model,Page<?>pageData,int blockSize){
        int currentPage= pageData.getNumber();
        int totalPages=pageData.getTotalPages();
        int startPage=(int)((Math.floor(currentPage/blockSize)*blockSize)+1);
        if(startPage>totalPages) startPage=totalPages;

        int endPage = Math.min(startPage + blockSize-1,totalPages);

        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
    }
}

