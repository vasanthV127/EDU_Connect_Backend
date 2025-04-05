package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.ERole;
import com.backendservice.EDU_Connect.model.Resource;
import com.backendservice.EDU_Connect.model.User;
import com.backendservice.EDU_Connect.payload.request.ResourceDTO;
import com.backendservice.EDU_Connect.repository.ResourceRepository;
import com.backendservice.EDU_Connect.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {


    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private UserRepository userRepo;

    // 📌 Student fetches all resources for their current & previous semesters
    @GetMapping("/resources")
    public List<ResourceDTO> getStudentResources(@RequestParam Long userId) {
        User student = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isStudent = student.getRoles().stream().anyMatch(role -> role.getName() == ERole.ROLE_USER);
        if (!isStudent) {
            throw new RuntimeException("Unauthorized Access!");
        }

        List<Resource> resources = resourceRepo.findAllByModuleSemesterLessThanEqual(student.getSemester());

        // Map Resource entities to ResourceDTO
        return resources.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private ResourceDTO mapToDTO(Resource resource) {
        ResourceDTO dto = new ResourceDTO();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setDescription(resource.getDescription());
        dto.setFileUrl(resource.getFileUrl());
        dto.setUploadedByName(resource.getUploadedBy().getName());
        dto.setSemester(resource.getModule().getSemester());
        return dto;
    }





}

