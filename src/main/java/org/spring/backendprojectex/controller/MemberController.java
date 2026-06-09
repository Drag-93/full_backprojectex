package org.spring.backendprojectex.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/join")
    public String join(){
        return "member/join";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }
}
