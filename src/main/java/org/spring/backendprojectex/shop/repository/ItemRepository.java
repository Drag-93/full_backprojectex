package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.shop.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Long> {
}
