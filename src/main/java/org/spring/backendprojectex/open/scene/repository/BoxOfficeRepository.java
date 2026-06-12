package org.spring.backendprojectex.open.scene.repository;

import org.spring.backendprojectex.open.scene.entity.boxoffice.BoxOfficeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoxOfficeRepository extends JpaRepository<BoxOfficeEntity,Long> {
    List<BoxOfficeEntity> findByMovieCd(String movieCd);
}
