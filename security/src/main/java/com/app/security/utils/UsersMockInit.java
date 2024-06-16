package com.app.security.utils;


import com.app.security.DTO.SignupDTO;
import com.app.security.entity.Profile;
import com.app.security.entity.ProfilePermission;
import com.app.security.entity.User;
import com.app.security.enumerated.PermissionList;
import com.app.security.enumerated.ProfileList;
import com.app.security.repository.PermissionRepository;
import com.app.security.repository.ProfilePermissionRepository;
import com.app.security.repository.ProfileRepository;
import com.app.security.repository.UserRepository;
import com.app.security.service.AuthenticationService;
import com.app.security.service.UserService;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsersMockInit {

    private static final Logger logger = LoggerFactory.getLogger(UsersMockInit.class);

    AuthenticationService authenticationService;

    UserService userService;
    UserRepository userRepository;
    ProfileRepository profileRepository;
    private final PermissionRepository permissionRepository;
    private final ProfilePermissionRepository profilePermissionRepository;


    @Autowired
    public UsersMockInit( UserService userService,
                          ProfileRepository profileRepository,
                          AuthenticationService authenticationService,
                          UserRepository userRepository,
                          PermissionRepository permissionRepository,
                          ProfilePermissionRepository profilePermissionRepository ) {
        this.userService = userService;
        this.profileRepository = profileRepository;
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.profilePermissionRepository = profilePermissionRepository;
    }

    @PostConstruct
    public void initUsers() {
        long numberOfusers = userRepository.count();

        logger.info("NUMERO DI UTENTI: " + numberOfusers);

        int userLimit = 100;

        if( numberOfusers >= userLimit ) return;

        logger.info("LIMITE UTENTI NON SUPERATO: " + userLimit);


        Faker faker = new Faker();

        String adminMail = "Admin@gmail.com";
        String adminUsername = "Admin";

        String adiMail = "Adiener@gmail.com";
        String adiUsername = "Adiener";


        for( int i = 0 ; i < 101 ; i++ ) {

            if( i == 0 ) {
                checkAndCreateUser( adminMail, adminUsername, i );
                continue;
            }
            if( i == 1 ) {
                checkAndCreateUser( adiMail, adiUsername, i );
                continue;
            }


            String email = faker.internet().emailAddress();
            String username = faker.name().username();

            checkAndCreateUser( email, username, i );

        }

        // Sblocco utente Admin
        User userAdmin = userRepository.findByEmail( "Adiener@gmail.com" ).orElseThrow();
        userAdmin.setEnabled(true);
        userAdmin.setTemporaryPassword( false );

        // Creazione profilo Admin
        Profile profileAdmin = userAdmin.getProfile();
        profileAdmin.setName( ProfileList.ADMIN );
        profileAdmin.setPower( 0 );
        profileRepository.save( profileAdmin );

        userAdmin.setProfile( profileAdmin );
        userRepository.save( userAdmin );


        setAllPermissions( profileAdmin );
    }

    private void setAllPermissions( Profile profileAdmin ) {
        for( PermissionList permissionList : PermissionList.values() ) {
            createProfilePermission( profileAdmin.getId(), permissionList, 1, 1, 1, 1 );
        }
    }

    private void checkAndCreateUser( String email, String username, int i ) {

        boolean userCheck = userService.existsByUsernameOrEmail( username, email );

        if( !userCheck ) {
            createUser( email, username );
            logger.info( "[" + i + "] Utente inizializzato: " + email );
        } else {
            logger.warn( "Utente ESISTENTE: " + email );
        }
    }

    private void createUser( String email, String username ) {
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setEmail( email );
        signupDTO.setUsername( username );


        authenticationService.createUser( signupDTO, false );
    }


    private void createProfilePermission(
            Long profileId,
            PermissionList permissionList,
            int valueCreate,
            int valueRead,
            int valueUpdate,
            int valueDelete
    ) {
        Profile profile = profileRepository.findByUserId( profileId );

        ProfilePermission profilePermission = ProfilePermission
                .builder()
                .profile( profile )
                .permission( permissionRepository.findByName( permissionList ).orElseThrow() )
                .valueCreate( valueCreate )
                .valueRead( valueRead )
                .valueUpdate( valueUpdate )
                .valueDelete( valueDelete )
                .build();

        profilePermissionRepository.save( profilePermission );
    }

}
