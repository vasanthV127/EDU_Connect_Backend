package com.backendservice.EDU_Connect.repository;

import com.backendservice.EDU_Connect.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findAllByModuleSemesterLessThanEqual(Integer semester);
}
