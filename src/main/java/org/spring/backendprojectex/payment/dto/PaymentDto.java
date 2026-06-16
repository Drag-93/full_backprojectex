package org.spring.backendprojectex.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.payment.entity.PaymentItemEntity;
import org.spring.backendprojectex.shop.entity.CartEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {
    @Schema(description = "결제 고유 ID", example = "1")
    private Long id;
    //주문금액
    @Schema(description = "결제 금액", example = "10000")
    private String payResult;
    @Schema(description = "결제 방법", example = "CARD")
    private String paymentType;
    @Schema(description = "주문처", example = "온라인 쇼핑물")
    private String orderPost;
    @Schema(description = "배송 주소", example = "서울시 노원구 상계동")
    private String orderAddr;
    @Schema(description = "수령 방식", example = "배달")
    private String orderMethod;
    @Schema(description = "회원 ID", example = "5")
    private Long memberId;
    @Schema(description = "장바구니 ID", example = "12")
    private Long cartId;
    @Schema(description = "회원 정보", hidden = true)
    private MemberEntity memberEntity;
    @Schema(description = "장바구니 목록", hidden = true)
    private List<CartEntity> cartEntities;
    @Schema(description = "결제에 포함된 아이템 목록")
    private List<PaymentItemEntity> paymentItemEntities;
    @Schema(description = "생성 시간", example = "2026-06-16T14:20:20")
    private LocalDateTime createTime;
    @Schema(description = "생성 시간", example = "2026-06-16T14:20:20")
    private LocalDateTime updateTime;

}