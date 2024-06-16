package com.app.security;


import com.app.security.entity.ProfilePermission;
import com.app.security.entity.User;
import com.app.security.exception.ErrorCodeList;
import com.app.security.exception.appException;
import com.app.security.repository.UserRepository;
import com.app.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public CustomUserDetailsService( UserService userService,
                                     UserRepository userRepository) {
        this.userService = userService;
    }


    /*** LOAD USER BY USERNAME
         * Questo metodo viene chiamato durante il processo di login e restituisce un oggetto UserDetails che contiene
         * le informazioni dell'utente, che verranno utilizzate da Spring Security per il controllo degli accessi.
         * Prende come input l'username o l'email dell'utente.
         * Inizialmente, cerca l'utente nel database utilizzando l'username o l'email forniti.
         * Se l'utente non esiste, viene lanciata un'eccezione UsernameNotFoundException.
         * Successivamente, crea un set di stringhe che conterrà i profili dell'utente e un set di GrantedAuthority
         * che conterrà i permessi dell'utente.
         * Recupera il set di UserPermission dell'utente e per ogni UserPermission, recupera il nome del permesso e
         * verifica se l'utente ha i permessi di lettura, scrittura, eliminazione e modifica.
         * Infine, crea un nuovo oggetto UserDetails con l'email dell'utente, la password dell'utente e il set di
         * GrantedAuthority, e lo restituisce.
     ***/
    @Override
    public UserDetails loadUserByUsername( String usernameOrEmail ) throws UsernameNotFoundException {

        // RECUPERO L'UTENTE DAL DATABASE
        User user = userService.findByUsernameOrEmail( usernameOrEmail, usernameOrEmail )
                .orElseThrow( () -> new UsernameNotFoundException( ErrorCodeList.NF404 ) );

        // SE L'UTENTE NON E' ABILITATO
        if( !user.isEnabled() ) throw new appException( HttpStatus.BAD_REQUEST, ErrorCodeList.NOTUSERENABLED );

        // CREO UN SET DI STRINGHE CHE CONTERRA' I PERMESSI DELL'UTENTE
        Set<String> permissions = new HashSet<>();
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        //SE LA PASS E' TEMPORANEA
        if(user.isTemporaryPassword()) {
            permissions.add("TEMPORARY_PASSWORD");
        } else {
            // CREO UN SET DI GRANTEDAUTHORITY CHE CONTERRA' I PERMESSI DELL'UTENTE
            Set<ProfilePermission> profilePermissions = userService.getProfilePermissionsByUserId(user.getId());


            // PER OGNI PROFILEPERMISSION RECUPERATO DAL SET
            for( ProfilePermission profilePermission : profilePermissions) {
                // RECUPERO IL NOME DEL PERMESSO
                String permName = profilePermission.getPermission().getName().name();

                String create = permName + "_CREATE";
                String read = permName + "_READ";
                String update = permName + "_UPDATE";
                String delete = permName + "_DELETE";

                // SE IL VALORE DEL PERMESSO E' UGUALE A 1
                // AGGIUNGO LA STRINGA CORRISPONDENTE AL PERMESSO AL SET DI PERMESSI
                if( profilePermission.getValueRead() == 1 ) {

                    permissions.add( read );
                }
                if( profilePermission.getValueCreate() == 1 ) {
                    permissions.add( create );
                }
                if( profilePermission.getValueDelete() == 1 ) {
                    permissions.add( delete );
                }
                if( profilePermission.getValueUpdate() == 1 ) {
                    permissions.add( update );
                }
            }
        }

        // PER OGNI PERMESSO RECUPERATO DAL SET DI PERMESSI
        for( String permission : permissions ) {
            // AGGIUNGO UN NUOVO OGGETTO GRANTEDAUTHORITY AL SET DI GRANTEDAUTHORITY
            grantedAuthorities.add( new SimpleGrantedAuthority( permission ) );
        }

        return new org.springframework.security.core.userdetails.User( user.getEmail(), user.getPassword(), grantedAuthorities );
    }
}
