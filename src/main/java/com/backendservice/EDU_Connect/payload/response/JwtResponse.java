package com.backendservice.EDU_Connect.payload.response;

import java.util.List;


public class JwtResponse {

    private String token;
    private String type ="Bearer";

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    private Long id;
    private String username;
    private List<String> roles;
    private int semester;


    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public JwtResponse(String token, Long id, String username, List<String> roles, int semester) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.semester=semester;
    }
}
