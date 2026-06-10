package org.spring.backendprojectex.member.repository;

import jakarta.validation.constraints.NotBlank;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByUserEmail(@NotBlank(message = "이메일를 입력하세요.") String userEmail);
}
