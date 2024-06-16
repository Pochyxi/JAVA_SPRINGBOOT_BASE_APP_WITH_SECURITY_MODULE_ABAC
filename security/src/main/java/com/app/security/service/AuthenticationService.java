package com.app.security.service;

import com.app.security.DTO.ChangePasswordDTO;
import com.app.security.DTO.JwtAuthResponseDTO;
import com.app.security.DTO.LoginDTO;
import com.app.security.DTO.SignupDTO;
import com.app.security.entity.Confirmation;
import com.app.security.entity.User;
import com.app.security.enumerated.TokenType;

public interface AuthenticationService {

    JwtAuthResponseDTO login( LoginDTO loginDTO);

    User createUser( SignupDTO signupDTO, boolean confEmail);

    void changeEmail( Long userId, String email);

    void changePassword( ChangePasswordDTO changePasswordDTO, String token);

    Confirmation verifyToken( String token, TokenType tokenType );

    void resetPasswordRequest(String email);

    void resendVerificationRequest(Long userId);
}