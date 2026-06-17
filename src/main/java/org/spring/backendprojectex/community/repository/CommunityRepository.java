package org.spring.backendprojectex.community.repository;

import org.spring.backendprojectex.community.entity.CommunityEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepository extends JpaRepository<CommunityEntity, Long> {
    Page<CommunityEntity> findAllByOrderByIdDesc(Pageable pageable);

    Page<CommunityEntity> findByTitleContainingOrderByIdDesc(String search, Pageable pageable);

    Page<CommunityEntity> findByContentContainingOrderByIdDesc(String search, Pageable pageable);

    Page<CommunityEntity> findByNickNameContainingOrderByIdDesc(String search, Pageable pageable);

    Page<CommunityEntity> findByCategoryContainingOrderByIdDesc(String search, Pageable pageable);
}
