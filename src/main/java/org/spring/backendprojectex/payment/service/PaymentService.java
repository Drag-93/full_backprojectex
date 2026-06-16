package org.spring.backendprojectex.payment.service;

import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentService {
    void insertPayment(PaymentDto paymentDto);

    List<PaymentDto> paymentListFn(Long memberId);

    List<PaymentDto> paymentAllList();
}
