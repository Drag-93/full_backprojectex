package org.spring.backendprojectex.payment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.spring.backendprojectex.common.BasicTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="paymentItem_tb06")
public class PaymentItemEntity extends BasicTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="paymentItem_id")
    private Long id;

    private String paymentItemTitle;

    private int paymentItemPrice;

    private int paymentItemSize;

    //N:1
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="payment_id")
    private PaymentEntity paymentEntity;
}
