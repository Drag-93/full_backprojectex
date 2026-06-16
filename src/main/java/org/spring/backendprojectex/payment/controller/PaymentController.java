package org.spring.backendprojectex.payment.controller;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.payment.repository.PaymentRepository;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.spring.backendprojectex.shop.dto.ItemListDto;
import org.spring.backendprojectex.shop.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final CartService cartService;
    private final PaymentService paymentService;

    @GetMapping("/{memberId}")
    public String index(@PathVariable("memberId")Long memberId, Model model){
        //장바구니 정보 get
        List<ItemListDto> itemListDtos=cartService.cartList(memberId);
        model.addAttribute("itemList",itemListDtos);
        model.addAttribute("memberId",memberId);
        System.out.println(itemListDtos);
        return "payment/index"; //자신의 결제 페이지
    }
    @PostMapping("/insert")
    @ResponseBody
    public String insert(@ModelAttribute PaymentDto paymentDto,Model model) {
        System.out.println(paymentDto);
        paymentService.insertPayment(paymentDto);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("code", 1); //성공 시 1 반환
        model.addAttribute("result", result);
        //결과를 -> paymentResult.html
        return "payment/paymentResult";
    }

    @PostMapping("/insert2")
    @ResponseBody
    public Map<String,Object> insert2(@RequestBody PaymentDto paymentDto){
        System.out.println(paymentDto);
        paymentService.insertPayment(paymentDto);
        Map<String, Object> result=new HashMap<>();
        result.put("status","success");
        result.put("code",1); //성공 시 1 반환
        return result;
    }

    @GetMapping("/paymentList/{memberId}")
    public String paymentList(@PathVariable("memberId") Long memberId, Model model){
        List<PaymentDto> paymentDtos=paymentService.paymentListFn(memberId);
        model.addAttribute("paymentList",paymentDtos);
        System.out.println(paymentDtos);
        return "payment/paymentList"; //자신의 결제내역 페이지
    }
}
