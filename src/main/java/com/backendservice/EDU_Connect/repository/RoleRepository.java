package com.backendservice.EDU_Connect.repository;


import com.backendservice.EDU_Connect.model.ERole;
import com.backendservice.EDU_Connect.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Long> {


    Optional<Role> findByName(ERole name);
}
