package org.spring.backendprojectex.member.repository;

import jakarta.validation.constraints.NotBlank;
import org.spring.backendprojectex.member.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByUserEmail(@NotBlank(message = "이메일를 입력하세요.") String userEmail);

    Page<MemberEntity> findByUserEmail(Pageable pageable, String search);

    Page<MemberEntity> findByUserName(Pageable pageable, String search);

    Page<MemberEntity> findByRole(Pageable pageable, String upperCase);
}
