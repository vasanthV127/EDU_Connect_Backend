        package com.backendservice.EDU_Connect.model;
    
        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonManagedReference;
        import jakarta.persistence.*;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.Size;
        import lombok.AllArgsConstructor;
        import lombok.Data;
    
    
        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.HashSet;
        import java.util.List;
        import java.util.Set;
    
        @Table(name = "users" ,uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
        @Entity
        @AllArgsConstructor
        @Data
        public class User {
    
    
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
    
    
            @Size(max =30)
            private String name;
    
    
            @Size(max =50)
            private String email;
    
            @Size(max =120)
            @NotBlank
            private String password;
    
            @ManyToMany(fetch = FetchType.LAZY)
            @JoinTable(name="user_roles",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name="role_id"))
            private Set<Role> roles = new HashSet<>();
    
            private Integer semester;
    
            // One teacher can upload multiple resources
            @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL, orphanRemoval = true)
            @JsonManagedReference
            private List<Resource> uploadedResources;
    
            @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
            private List<Message> sentMessages = new ArrayList<>();
    
            @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
            private List<Message> receivedMessages = new ArrayList<>();
    
            private String resetOtp;
            private LocalDateTime otpExpiry;
    
            public String getResetOtp() {
                return resetOtp;
            }
    
            public void setResetOtp(String resetOtp) {
                this.resetOtp = resetOtp;
            }
    
            public LocalDateTime getOtpExpiry() {
                return otpExpiry;
            }
    
            public void setOtpExpiry(LocalDateTime otpExpiry) {
                this.otpExpiry = otpExpiry;
            }
    
            public List<Message> getSentMessages() {
                return sentMessages;
            }
    
            public void setSentMessages(List<Message> sentMessages) {
                this.sentMessages = sentMessages;
            }
    
            public List<Message> getReceivedMessages() {
                return receivedMessages;
            }
    
            public void setReceivedMessages(List<Message> receivedMessages) {
                this.receivedMessages = receivedMessages;
            }
    
            public User(String email, String password, String name){
                this.email =email;
                this.password=password;
                this.name = name;
    
    
            }
            public User() {
            }
    
            public @Size(max = 30) String getName() {
                return name;
            }
    
            public void setName(@Size(max = 30) String name) {
                this.name = name;
            }
    
            public @Size(max = 50) String getEmail() {
                return email;
            }
    
            public void setEmail(@Size(max = 50) String email) {
                this.email = email;
            }
    
            public @Size(max = 120) @NotBlank String getPassword() {
                return password;
            }
    
            public void setPassword(@Size(max = 120) @NotBlank String password) {
                this.password = password;
            }
    
            public Set<Role> getRoles() {
                return roles;
            }
    
            public void setRoles(Set<Role> roles) {
                this.roles = roles;
            }
    
            public Integer getSemester() {
                return semester;
            }
    
            public void setSemester(Integer semester) {
                this.semester = semester;
            }
    
            public List<Resource> getUploadedResources() {
                return uploadedResources;
            }
    
            public void setUploadedResources(List<Resource> uploadedResources) {
                this.uploadedResources = uploadedResources;
            }
        }
    
    
