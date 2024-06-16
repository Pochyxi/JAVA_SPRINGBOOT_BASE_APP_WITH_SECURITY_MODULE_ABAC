package com.app.security.DTO;

import lombok.Data;

@Data
public class LoginDTO {

    private String usernameOrEmail;

    private String password;
}