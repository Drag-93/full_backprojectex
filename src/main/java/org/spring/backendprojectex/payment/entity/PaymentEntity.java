package org.spring.backendprojectex.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.CartEntity;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="payment_tb06")
public class PaymentEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Long id;
    //결제방법
    private String paymentType;
    //주문처
    private String orderPost;
    //배송주소
    private String orderAddr;
    //주문금액
    private String payResult;
    //주문방법
    private String orderMethod;

    //N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private MemberEntity memberEntity;

    //1:N
    @JsonIgnore
    @OneToMany(mappedBy = "paymentEntity",
    fetch = FetchType.LAZY,
    cascade = CascadeType.REMOVE)
    private List<CartEntity> cartEntities;

    //1:N
    @JsonIgnore
    @OneToMany(mappedBy = "paymentEntity",
    fetch = FetchType.LAZY,
    cascade = CascadeType.REMOVE)
    private List<PaymentItemEntity> paymentItemEntities;



}
