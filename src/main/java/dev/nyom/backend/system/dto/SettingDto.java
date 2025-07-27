package dev.nyom.backend.system.dto;

import dev.nyom.backend.system.model.Setting.SettingType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Setting public schema.")
public class SettingDto {
    @Schema(description = "Identifier of the setting", example = "theme")
    private String key;

    @Schema(description = "Value of the setting", example = "dark")
    private String value;

    @Schema(description = "Type of the setting", example = "USER")
    private SettingType type;
}
