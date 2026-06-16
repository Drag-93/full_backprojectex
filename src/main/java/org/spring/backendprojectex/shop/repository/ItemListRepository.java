package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.shop.entity.ItemListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemListRepository extends JpaRepository<ItemListEntity,Long> {
    List<ItemListEntity> findByCartEntityIdAndItemEntityId(Long id, Long id1);

    List<ItemListEntity> findAllByCartEntityId(Long id);
}
