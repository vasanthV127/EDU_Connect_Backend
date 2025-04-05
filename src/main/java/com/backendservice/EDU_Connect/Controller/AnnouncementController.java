package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.Announcement;
import com.backendservice.EDU_Connect.model.Module;
import com.backendservice.EDU_Connect.model.User;
import com.backendservice.EDU_Connect.payload.request.AnnouncementDTO;
import com.backendservice.EDU_Connect.repository.AnnouncementRepository;
import com.backendservice.EDU_Connect.repository.ModuleRepository;
import com.backendservice.EDU_Connect.repository.UserRepository;
import com.backendservice.EDU_Connect.security.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AnnouncementController {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @PostMapping("/modules/{moduleId}/announcements")
    public ResponseEntity<?> createAnnouncement(
            @PathVariable Long moduleId,
            @RequestBody AnnouncementRequest announcementRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        Optional<User> userOpt = userRepository.findByEmail(currentUserEmail);
        if (!userOpt.isPresent()) {
            return ResponseEntity.status(401).body(null);
        }
        User user = userOpt.get();

        // Check if user has ROLE_TEACHER
//        boolean isTeacher = user.getRoles().stream()
//                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
//        if (!isTeacher) {
//            return ResponseEntity.status(403).body(null);
//        }

        Optional<Module> moduleOpt = moduleRepository.findById(moduleId);
        if (!moduleOpt.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        Module module = moduleOpt.get();

        Announcement announcement = new Announcement();
        announcement.setTitle(announcementRequest.getTitle());
        announcement.setContent(announcementRequest.getContent());
        announcement.setDatePosted(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        announcement.setModule(module);
        announcement.setPostedBy(user);

        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Send email to students in the same semester
        List<User> students = userRepository.findBySemester(module.getSemester());
        for (User student : students) {
            if (!student.getId().equals(user.getId())) { // Don't send to the teacher
                emailService.sendAnnouncementEmail(
                        student.getEmail(),
                        announcement.getTitle(),
                        announcement.getContent(),
                        user.getName()
                );
            }
        }

        return ResponseEntity.ok("Successfully Announced !!!");
    }

    // Other existing methods remain unchanged
    @GetMapping("/modules/{moduleId}/announcements")
    public ResponseEntity<List<Announcement>> getAnnouncementsByModule(@PathVariable Long moduleId) {
        List<Announcement> announcements = announcementRepository.findByModuleId(moduleId);
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/modules/user/{userId}/announcements")
    public ResponseEntity<List<AnnouncementDTO>> getUserAnnouncements(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User user = userOpt.get();
        List<Module> modules = moduleRepository.findBySemester(user.getSemester());
        List<AnnouncementDTO> announcementDTOs = modules.stream()
                .flatMap(module -> module.getAnnouncements().stream())
                .map(this::mapToAnnouncementDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(announcementDTOs);
    }

    private AnnouncementDTO mapToAnnouncementDTO(Announcement announcement) {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setContent(announcement.getContent());
        dto.setDatePosted(announcement.getDatePosted());
        dto.setModuleId(announcement.getModule().getId());
        dto.setPostedById(announcement.getPostedBy().getId());
        dto.setPostedByName(announcement.getPostedBy().getName());
        return dto;
    }

    public static class AnnouncementRequest {
        private String title;
        private String content;

        // Getters and setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}