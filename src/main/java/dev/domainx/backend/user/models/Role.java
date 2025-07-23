package dev.domainx.backend.user.models;

import java.util.HashSet;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Schema(description = "Defines a role which groups a set of permissions.")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the role", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique name of the role", example = "ADMIN")
    private String name;

    @ManyToMany(targetEntity = Permission.class, fetch = FetchType.LAZY)
    @JoinTable(
        name = "roles_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Schema(description = "Set of permissions assigned to this role")
    private Set<Permission> permissions = new HashSet<>();
}
