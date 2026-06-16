package org.spring.backendprojectex.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.payment.entity.PaymentEntity;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="cart_tb06")
public class CartEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    //1:1 -> 한쪽만 설정
    @OneToOne
    @JoinColumn(name="member_id")
    private MemberEntity memberEntity;

    //1:N
    @OneToMany(mappedBy = "cartEntity",
    fetch = FetchType.LAZY,
    cascade = CascadeType.REMOVE)
    private List<ItemListEntity> itemListEntities;


    //N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="payment_id")
    private PaymentEntity paymentEntity;

}
