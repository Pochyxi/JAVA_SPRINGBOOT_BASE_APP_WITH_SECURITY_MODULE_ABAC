package com.app.security.controller;


import com.app.security.DTO.ChangePasswordDTO;
import com.app.security.DTO.JwtAuthResponseDTO;
import com.app.security.DTO.LoginDTO;
import com.app.security.DTO.SignupDTO;
import com.app.security.enumerated.TokenType;
import com.app.security.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController( AuthenticationService authenticationService ) {
        this.authenticationService = authenticationService;
    }

    /**
     * LOGIN
     * Questo metodo permette di effettuare il login
     * PREAUTHORIZATION: NONE
     */
    @PostMapping(value = "/login")
    public ResponseEntity<JwtAuthResponseDTO> login( @RequestBody LoginDTO loginDTO ) {
        JwtAuthResponseDTO jwtAuthResponseDTO = authenticationService.login( loginDTO );


        return new ResponseEntity<>( jwtAuthResponseDTO, HttpStatus.OK );
    }

    /**
     * SIGNUP
     * Questo metodo permette di effettuare la registrazione
     * PREAUTHORIZATION: NONE
     * TODO: Eliminare in produzione
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupDTO signupDTO ) {
        authenticationService.createUser( signupDTO, true );

        return new ResponseEntity<>(HttpStatus.CREATED );
    }

    /**
     * CREAZIONE UTENTE
        * Questo metodo permette di Creare un utente
        * PREAUTHORIZATION:
             * Utente con permesso USER_CREATE
        *  TODO: eliminare il produzione
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<Void>adminCreateUser(@Valid @RequestBody SignupDTO signupDTO) {

        authenticationService.createUser( signupDTO, true );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * CHANGE PASSWORD
         * Questo metodo permette di modificare la password
         * PREAUTHORIZATION:
             * 1 - E' possibile cambiare la password avendo a disposizione il token inviato tramite email
             * 2 - Attraverso autenticazione specificando username e vecchia password
     */
    @PutMapping(value = "/change_password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO,
                                               @RequestParam(value = "token", required = false) String token ) {
        authenticationService.changePassword( changePasswordDTO, token );

        return new ResponseEntity<>( HttpStatus.OK );
    }


    @PutMapping(value = "/change_email")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void> changeEmail(@RequestParam Long userId,
                                            @RequestParam String email){
        authenticationService.changeEmail( userId, email );

        return new ResponseEntity<>( HttpStatus.OK );
    }

    @GetMapping(value = "/recovery_password")
    public ResponseEntity<Void> recovery(@RequestParam("email") String email) {
        authenticationService.resetPasswordRequest(email);

        return new ResponseEntity<Void> (HttpStatus.OK);
    }

    @PutMapping(value = "/resend_verification")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    public ResponseEntity<Void> resendVerification(@RequestParam Long userId) {
        authenticationService.resendVerificationRequest(userId);
        return new ResponseEntity<Void> (HttpStatus.OK);
    }

    @GetMapping(value = "/verify")
    public ResponseEntity<Void> confirm(@RequestParam("token") String token,
                                        @RequestParam("tokentype")String tokentype) {
        authenticationService.verifyToken( token, TokenType.valueOf(tokentype));

        return new ResponseEntity<>( HttpStatus.OK );
    }

}
