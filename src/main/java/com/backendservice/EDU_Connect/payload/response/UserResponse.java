package com.backendservice.EDU_Connect.payload.response;


import java.util.Set;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Integer semester;
    private Set<String> roles;

    public UserResponse(Long id, String name, String email, Integer semester, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.semester = semester;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
