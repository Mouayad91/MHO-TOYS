package com.mho_toys.backend.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "roles")
public class Role {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "role_name")
    private ApplicationRole roleName;

    public Role(ApplicationRole roleName) {
        this.roleName = roleName;
    }

    public ApplicationRole getRoleName() {
        return roleName;
    }

    public void setRoleName(ApplicationRole roleName) {
        this.roleName = roleName;
    }
}