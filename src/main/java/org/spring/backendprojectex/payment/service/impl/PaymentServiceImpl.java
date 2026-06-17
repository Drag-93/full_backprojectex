package org.spring.backendprojectex.payment.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.member.repository.MemberRepository;
import org.spring.backendprojectex.payment.dto.PaymentDto;
import org.spring.backendprojectex.payment.entity.PaymentEntity;
import org.spring.backendprojectex.payment.entity.PaymentItemEntity;
import org.spring.backendprojectex.payment.repository.PaymentItemRepository;
import org.spring.backendprojectex.payment.repository.PaymentRepository;
import org.spring.backendprojectex.payment.service.PaymentService;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.spring.backendprojectex.shop.repository.CartRepository;
import org.spring.backendprojectex.shop.repository.ItemListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final MemberRepository memberRepository;
    private final ItemListRepository itemListRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentItemRepository paymentItemRepository;
    private final CartRepository cartRepository;
    @Override
    public void insertPayment(PaymentDto paymentDto) {
        //1. 회원 정보 조회
        MemberEntity memberEntity = memberRepository.findById(paymentDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원 정보가 없습니다."));
        //2. 장바구니 상품 목록 조회
        List<ItemListEntity> itemListEntities=itemListRepository.findAllByCartEntityId(paymentDto.getCartId());
        if(itemListEntities.isEmpty()){
            throw new IllegalArgumentException("장바구니가 비어있습니다.");
        }
        //3. 결제 마스터 정보(PaymentEntity)저장
        PaymentEntity paymentEntity=PaymentEntity.builder()
                .paymentType(paymentDto.getPaymentType())
                .orderMethod(paymentDto.getOrderMethod())
                .orderPost(paymentDto.getOrderPost())
                .payResult(paymentDto.getPayResult())
                .orderAddr(paymentDto.getOrderAddr())
                .memberEntity(memberEntity)
                .build();
        PaymentEntity savedPayment=paymentRepository.save(paymentEntity);

        //4. 장바구니 내역을 결제 상세 내역(PaymentItemEntity)스냅샷으로 변환
        List<PaymentItemEntity>paymentItemEntities=itemListEntities.stream().map(el->
            PaymentItemEntity.builder()
                    .paymentItemTitle(el.getItemEntity().getItemTitle())
                    .paymentItemPrice(el.getItemEntity().getItemPrice())
                    .paymentItemSize(el.getItemSize())
                    .paymentEntity(savedPayment)
                    .build()).toList();
        //5. 결제 상세 상품 대량 저장(Bulk Insert 효과)
        paymentItemRepository.saveAll(paymentItemEntities);
        //6. 장바구니 비우기(결제 완료 시점에 실행)
        CartEntity cartEntity=cartRepository.findByMemberEntityId(paymentDto.getMemberId())
                .orElseThrow(()->new IllegalArgumentException("삭제할 장바구니가 존재하지 않습니다."));
        cartRepository.delete(cartEntity);
    }
    @Transactional(readOnly = true)
    @Override
    public List<PaymentDto> paymentListFn(Long memberId) {
        List<PaymentEntity> paymentEntities= paymentRepository.findAllByMemberEntityIdOrderByIdDesc(memberId);

        System.out.println(paymentEntities.toString());
        return paymentEntities.stream().map(el->PaymentDto.builder()
                .paymentType(el.getPaymentType())
                .orderAddr(el.getOrderAddr())
                .orderMethod(el.getOrderMethod())
                .orderPost(el.getOrderPost())
                .payResult(el.getPayResult())
                .memberId(el.getMemberEntity().getId())
                .paymentItemEntities(el.getPaymentItemEntities())
                .createTime(el.getCreateTime())
                .build()).toList();
    }

    @Override
    public List<PaymentDto> paymentAllList() {

        return  paymentRepository.findAll().stream().map(el->
                PaymentDto.builder()
                        .paymentType(el.getPaymentType())
                        .orderAddr(el.getOrderAddr())
                        .orderMethod(el.getOrderMethod())
                        .orderPost(el.getOrderPost())
                        .payResult(el.getPayResult())
                        .memberId(el.getMemberEntity().getId())
                        .paymentItemEntities(el.getPaymentItemEntities())
                        .build()).toList();
    }

}
