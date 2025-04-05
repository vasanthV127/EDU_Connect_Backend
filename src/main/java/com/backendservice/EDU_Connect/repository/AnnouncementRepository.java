package com.backendservice.EDU_Connect.repository;

import com.backendservice.EDU_Connect.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
    List<Announcement> findByModuleId(Long moduleId);
}