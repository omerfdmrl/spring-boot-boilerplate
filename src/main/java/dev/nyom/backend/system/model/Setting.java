package dev.nyom.backend.system.model;

import dev.nyom.backend.user.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "settings")
@Schema(description = "Represents user and system settings.")
public class Setting {
    public enum SettingType {
        SYSTEM("SYSTEM"),
        USER("USER");

        private final String value;

        SettingType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static SettingType fromValue(String value) {
            for (SettingType type : values()) {
                if (type.value.equalsIgnoreCase(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown setting type: " + value);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the setting", example = "1")
    private Long id;

    @Column(name = "key", nullable = false)
    @Schema(description = "Identifier of the setting", example = "theme")
    private String key;

    @Column(name = "value")
    @Schema(description = "Value of the setting", example = "dark")
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Schema(description = "Type of the setting", example = "USER")
    private SettingType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Schema(description = "The user this setting is associated with")
    private User user;
}
