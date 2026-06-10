package org.spring.backendprojectex.member.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @GetMapping("/join")
    public String joinPage(MemberDto memberDto){

        return "member/join";
    }

    //회원가입 실행 -> 유효성 검사
    @PostMapping("/join")
    public String joinPost(@Valid MemberDto memberDto ,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "member/join";
        }
        // 회원가입 실행
        memberService.memberInsert(memberDto);
        return "member/login";
    }
    //로그인페이지
    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        Object errorMessage = request.getSession().getAttribute("loginErrorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            request.getSession().removeAttribute("loginErrorMessage"); // 1회성 메시지
        }
        return "member/login";
    }

}
