package com.backendservice.EDU_Connect.repository;

import com.backendservice.EDU_Connect.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findBySemester(Integer semester);
}

