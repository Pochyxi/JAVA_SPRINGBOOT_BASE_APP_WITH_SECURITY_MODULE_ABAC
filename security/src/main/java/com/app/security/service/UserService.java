package com.app.security.service;


import com.app.security.DTO.PagedResponseDTO;
import com.app.security.DTO.ProfilePermissionDTO;
import com.app.security.DTO.UserDTO;
import com.app.security.entity.ProfilePermission;
import com.app.security.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    // VOID RETURNS
    void createUser( User user );

    void deleteUser(Long id );


    // USER RETURNS
    User save( User user );

    User findById( Long id );

    Optional<User> findByEmail( String email );

    Optional<User> findByUsernameOrEmail( String username, String email );

    User getUserByAuthentication();

    Page<User> findByProfilePowerGreaterThanEqual( int power, Pageable page);

    User mapUserDTOToEntity( UserDTO userDTO);


    // BOOLEAN RETURNS
    Boolean existsByUsername( String username );

    Boolean existsByEmail( String email );

    Boolean existsByUsernameOrEmail( String username, String email );


    // DTO RETURNS
    UserDTO getUserDTOById( Long id );

    PagedResponseDTO<UserDTO> getAllUsers( int pageNo, int pageSize, String sortBy, String sortDir );

    PagedResponseDTO<UserDTO> getByEmailContains( String email, int pageNo, int pageSize, String sortBy, String sortDir);

    UserDTO mapUserToDTO( User user);

    ProfilePermissionDTO mapProfilePermissionToDTO( ProfilePermission profilePermission );

    UserDTO modifyUser( Long id, UserDTO userDTO );

    Set<ProfilePermission> getProfilePermissionsByUserId( Long userId );


}
