package dev.nyom.backend.user.model;

import java.io.Serializable;
import java.util.Map;

import dev.nyom.backend.user.converter.SocialAccountsConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_profiles")
@Schema(description = "Represents user's profile informations.")
public class UserProfile implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user's profile", example = "1")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "User whose information is kept")
    private User user;

    @Column(columnDefinition = "TEXT")
    @Schema(description = "User's biography information")
    private String bio;

    @Column(length = 255)
    @Schema(description = "User's avatar image as uri")
    private String avatar;

    @Column(length = 255)
    @Schema(description = "User's owned website")
    private String website;

    @Column(length = 255)
    @Schema(description = "User's location")
    private String location;

    @Column(name = "social_accounts", columnDefinition = "TEXT")
    @Convert(converter = SocialAccountsConverter.class)
    @Schema(description = "User's social accounts")
    private Map<String, String> socialAccounts;
}
