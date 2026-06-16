package org.spring.backendprojectex.payment.dto;

import lombok.*;
import org.spring.backendprojectex.payment.entity.PaymentEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentItemDto {
    private Long id;
    private String paymentItemTitle;
    private int paymentItemPrice;
    private int paymentItemSize;
    private PaymentEntity paymentEntity;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
