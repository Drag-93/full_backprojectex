package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.member.entity.MemberEntity;
import org.spring.backendprojectex.shop.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long> {
    int countByMemberEntity(MemberEntity build);

    Optional<CartEntity> findByMemberEntityId(Long id);
}
