package org.spring.backendprojectex.payment.repository;

import org.spring.backendprojectex.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity,Long> {
    List<PaymentEntity> findAllByMemberEntityId(Long memberId);

    List<PaymentEntity> findAllByMemberEntityIdOrderByIdDesc(Long memberId);
}
