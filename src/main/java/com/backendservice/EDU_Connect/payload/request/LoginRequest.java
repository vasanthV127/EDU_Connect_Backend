package com.backendservice.EDU_Connect.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {


    @NotBlank
    @Size(max=50)
    private String email;

    @NotBlank
    private String password;

    private String semester;

    public @NotBlank String getPassword() {
        return password;
    }

    public @NotBlank @Size(max = 50) String getEmail() {
        return email;
    }

    public String getSemester() {
        return semester;
    }
}

