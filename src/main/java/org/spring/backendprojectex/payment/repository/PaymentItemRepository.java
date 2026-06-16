package org.spring.backendprojectex.payment.repository;

import org.spring.backendprojectex.payment.entity.PaymentItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentItemRepository extends JpaRepository<PaymentItemEntity,Long> {
}
