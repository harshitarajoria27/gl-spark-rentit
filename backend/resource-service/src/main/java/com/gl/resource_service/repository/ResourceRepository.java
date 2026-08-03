package com.gl.resource_service.repository;

import com.gl.resource_service.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByOwnerId(Long ownerId);

    List<Resource> findByCategory(String category);

    List<Resource> findByAvailableTrue();
}

