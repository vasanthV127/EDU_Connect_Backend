package com.backendservice.EDU_Connect.repository;


import com.backendservice.EDU_Connect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
    Optional<User> findByName(String name);
    List<User> findBySemester(Integer semester);
}
