package org.spring.backendprojectex.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.spring.backendprojectex.member.dto.MemberDto;
import org.spring.backendprojectex.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Admin", description = "관리자페이지 관련 API")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminRestApiController {
    private final MemberService memberService;


    @Operation(summary = "회원조회", description = "관리자권한 결제 조회",
            responses = {
                    @ApiResponse(responseCode = "404", description = "해당 ID의 회원 정보가 존재하지 않음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            })
    @GetMapping("/member/detail/{id}")
    public ResponseEntity<?> memberDetail(@PathVariable("id") Long id) {
        Map<String, MemberDto> map = new HashMap<>();
        MemberDto memberDto = memberService.memberDetail(id);
        map.put("member", memberDto);

        log.info("======memberDto=" + memberDto + "=====");
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }
}
