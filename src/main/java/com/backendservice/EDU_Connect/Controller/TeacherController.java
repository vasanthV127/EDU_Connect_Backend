package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.ERole;
import com.backendservice.EDU_Connect.model.Module;
import com.backendservice.EDU_Connect.model.Resource;
import com.backendservice.EDU_Connect.model.User;
import com.backendservice.EDU_Connect.repository.ModuleRepository;
import com.backendservice.EDU_Connect.repository.ResourceRepository;
import com.backendservice.EDU_Connect.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private ModuleRepository moduleRepo;

    @Autowired
    private UserRepository userRepo;

    private static final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload/{moduleId}")
    public String uploadResource(@PathVariable Long moduleId,
                                 @RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam MultipartFile file,
                                 @RequestParam Long teacherId) throws IOException {
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isTeacher = teacher.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);
        if (!isTeacher) {
            return "Only teachers can upload resources.";
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        String fileUrl = "/teacher/download/" + fileName;

        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setFileUrl(fileUrl);
        resource.setModule(module);
        resource.setUploadedBy(teacher);

        resourceRepo.save(resource);
        logger.info("Resource uploaded: {}", fileUrl);
        return "Resource uploaded successfully!";
    }

    @GetMapping("/resources/{teacherId}")
    public List<Resource> getUploadedResources(@PathVariable Long teacherId) {
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isTeacher = teacher.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);
        if (!isTeacher) {
            throw new RuntimeException("Unauthorized Access!");
        }

        return teacher.getUploadedResources();
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<FileSystemResource> downloadFile(@PathVariable String filename) {
        logger.debug("Attempting to download file: {}", filename);
        File file = new File(UPLOAD_DIR + filename);
        if (!file.exists()) {
            logger.error("File not found: {}", file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        logger.info("Serving file: {}", file.getAbsolutePath());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

    @GetMapping("/view/{filename}")
    public ResponseEntity<FileSystemResource> viewFile(@PathVariable String filename) {
        logger.debug("Attempting to view file: {}", filename);
        File file = new File(UPLOAD_DIR + filename);
        if (!file.exists()) {
            logger.error("File not found: {}", file.getAbsolutePath());
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);
        String contentType = "application/pdf"; // Adjust based on file type if needed

        logger.info("Serving file for view: {}", file.getAbsolutePath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                .contentType(MediaType.parseMediaType(contentType))
                .contentLength(file.length())
                .body(resource);
    }

    @PutMapping("/resources/{resourceId}")
    public ResponseEntity<Resource> updateResource(
            @PathVariable Long resourceId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam Long teacherId) throws IOException {
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isTeacher = teacher.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);
        if (!isTeacher) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        resource.setTitle(title);
        resource.setDescription(description);

        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.write(filePath, file.getBytes());
            String fileUrl = "/teacher/download/" + fileName;
            resource.setFileUrl(fileUrl);
        }

        resourceRepo.save(resource);
        logger.info("Resource updated: {}", resource.getFileUrl());
        return ResponseEntity.ok(resource);
    }

    // Delete Resource
    @DeleteMapping("/resources/{resourceId}")
    public ResponseEntity<String> deleteResource(
            @PathVariable Long resourceId,
            @RequestParam Long teacherId) {
        User teacher = userRepo.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isTeacher = teacher.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_ADMIN);
        if (!isTeacher) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
        }

        Resource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        // Optionally delete the file from the filesystem
        File file = new File(UPLOAD_DIR + resource.getFileUrl().substring("/teacher/download/".length()));
        if (file.exists()) {
            file.delete();
        }

        resourceRepo.delete(resource);
        logger.info("Resource deleted: {}", resourceId);
        return ResponseEntity.ok("Resource deleted successfully");
    }

    // Get Resources by Module
    @GetMapping("/modules/{moduleId}/resources")
    public List<Resource> getResourcesByModule(@PathVariable Long moduleId) {
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        return module.getResources();
    }
}