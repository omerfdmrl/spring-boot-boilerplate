package dev.nyom.backend.user.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
@Schema(description = "Represents an application user with credentials and roles.")
public class User {
    public User(String name, String username, String email, String password, Set<Role> roles) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Full name of the user", example = "John Doe")
    private String name;

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique username of the user", example = "johndoe")
    private String username;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email address of the user", example = "john@example.com")
    private String email;

    @Column(nullable = false)
    @Schema(description = "Encoded password of the user", example = "$2a$10$...")
    private String password;

    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "Set of roles assigned to the user")
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Date and time when the user was created", example = "2025-07-22T10:00:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Date and time when the user was last updated", example = "2025-07-22T12:30:00")
    private LocalDateTime updatedAt;
}
