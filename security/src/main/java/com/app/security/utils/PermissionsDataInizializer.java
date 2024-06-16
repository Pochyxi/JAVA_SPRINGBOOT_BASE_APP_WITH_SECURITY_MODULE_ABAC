package com.app.security.utils;

import com.app.security.entity.Permission;
import com.app.security.enumerated.PermissionList;
import com.app.security.repository.PermissionRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionsDataInizializer {
    private static final Logger logger = LoggerFactory.getLogger( UsersMockInit.class);

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionsDataInizializer( PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @PostConstruct
    public void initPermissions() {
        for ( PermissionList perm : PermissionList.values()) {
            permissionRepository.findByName(perm)
                    .orElseGet(() -> {
                        Permission newPerm = new Permission();
                        newPerm.setName(perm);
                        System.out.println("Permesso " + perm + " Creato");
                        return permissionRepository.save(newPerm);
                    });
        }

        logger.info("PERMESSI INIZIALIZZATI CORRETTAMENTE");
    }
}
