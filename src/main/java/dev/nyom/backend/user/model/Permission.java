package dev.nyom.backend.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions")
@Schema(description = "Represents a single permission (capability) in the system.")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the permission", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Unique name of the permission", example = "READ_USER")
    private String name;
}
