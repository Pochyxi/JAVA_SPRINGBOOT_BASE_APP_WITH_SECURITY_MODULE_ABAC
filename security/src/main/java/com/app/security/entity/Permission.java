package com.app.security.entity;

import com.app.security.enumerated.PermissionList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private PermissionList name;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL)
    private Set<ProfilePermission> profilePermissions;

    public PermissionList getName() {
        return name;
    }

}
