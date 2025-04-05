package com.backendservice.EDU_Connect.Controller;

import com.backendservice.EDU_Connect.model.ERole;
import com.backendservice.EDU_Connect.model.Role;
import com.backendservice.EDU_Connect.model.User;
import com.backendservice.EDU_Connect.payload.response.MessageResponse;
import com.backendservice.EDU_Connect.repository.RoleRepository;
import com.backendservice.EDU_Connect.repository.UserRepository;
import com.backendservice.EDU_Connect.security.services.EmailService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
@RestController
public class BulkSignupController {

    private static final Logger logger = LoggerFactory.getLogger(BulkSignupController.class);
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int PASSWORD_LENGTH = 12;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    EmailService emailService;

    // DTO for bulk signup request
    public static class StudentRequest {
        private String email;
        private String name;
        private Integer semester;

        // Getters and Setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getSemester() { return semester; }
        public void setSemester(Integer semester) { this.semester = semester; }
    }

    // Generate random password
    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return password.toString();
    }

    @PostMapping("/bulk-signup")
    public ResponseEntity<?> bulkSignup(@RequestBody List<StudentRequest> students) {
        List<String> results = new ArrayList<>();

        for (StudentRequest student : students) {
            try {
                Optional<User> existingUser = userRepository.findByEmail(student.getEmail());

                if (existingUser.isPresent()) {
                    // User exists, update semester
                    User user = existingUser.get();
                    user.setSemester(student.getSemester());
                    userRepository.save(user);

                    String message = String.format("Your account has been updated for semester %d",
                            student.getSemester());
                    emailService.sendSimpleEmail(
                            student.getEmail(),
                            "Semester Update Notification",
                            message
                    );
                    results.add("Updated semester for: " + student.getEmail());
                } else {
                    // New user registration
                    String randomPassword = generateRandomPassword();
                    User user = new User(student.getEmail(),
                            encoder.encode(randomPassword),
                            student.getName());
                    user.setSemester(student.getSemester());

                    // Assign default ROLE_USER
                    Set<Role> roles = new HashSet<>();
                    Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found!"));
                    roles.add(userRole);
                    user.setRoles(roles);

                    userRepository.save(user);

                    String message = String.format(
                            "Your account has been created!\n" +
                                    "Email: %s\n" +
                                    "Password: %s\n" +
                                    "Semester: %d",
                            student.getEmail(), randomPassword, student.getSemester()
                    );
                    emailService.sendSimpleEmail(
                            student.getEmail(),
                            "Welcome to EDU_Connect",
                            message
                    );
                    results.add("Registered new user: " + student.getEmail());
                }
            } catch (Exception e) {
                logger.error("Error processing student {}: {}", student.getEmail(), e.getMessage());
                results.add("Error processing: " + student.getEmail());
            }
        }

        return ResponseEntity.ok(new MessageResponse(String.join("\n", results)));
    }

    @PostMapping("/bulk-signup-excel")
    public ResponseEntity<?> bulkSignupFromExcel(@RequestParam("file") MultipartFile file) {
        List<String> results = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = sheet.iterator();
            // Skip header row if exists
            if (rowIterator.hasNext()) rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String email = row.getCell(0).getStringCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    int semester = (int) row.getCell(2).getNumericCellValue();

                    Optional<User> existingUser = userRepository.findByEmail(email);

                    if (existingUser.isPresent()) {
                        User user = existingUser.get();
                        user.setSemester(semester);
                        userRepository.save(user);

                        String message = String.format("Your account has been updated for semester %d",
                                semester);
                        emailService.sendSimpleEmail(
                                email,
                                "Semester Update Notification",
                                message
                        );
                        results.add("Updated semester for: " + email);
                    } else {
                        String randomPassword = generateRandomPassword();
                        User user = new User(email, encoder.encode(randomPassword), name);
                        user.setSemester(semester);

                        Set<Role> roles = new HashSet<>();
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found!"));
                        roles.add(userRole);
                        user.setRoles(roles);

                        userRepository.save(user);

                        String message = String.format(
                                "Your account has been created!\n" +
                                        "Email: %s\n" +
                                        "Password: %s\n" +
                                        "Semester: %d",
                                email, randomPassword, semester
                        );
                        emailService.sendSimpleEmail(
                                email,
                                "Welcome to EDU_Connect",
                                message
                        );
                        results.add("Registered new user: " + email);
                    }
                } catch (Exception e) {
                    logger.error("Error processing row {}: {}", row.getRowNum(), e.getMessage());
                    results.add("Error processing row " + row.getRowNum());
                }
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error reading Excel file: " + e.getMessage()));
        }

        return ResponseEntity.ok(new MessageResponse(String.join("\n", results)));
    }
}