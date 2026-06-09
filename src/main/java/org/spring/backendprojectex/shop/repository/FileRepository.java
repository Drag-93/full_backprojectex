package org.spring.backendprojectex.shop.repository;

import org.spring.backendprojectex.shop.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
