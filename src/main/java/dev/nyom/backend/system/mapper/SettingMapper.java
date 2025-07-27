package dev.nyom.backend.system.mapper;

import org.springframework.stereotype.Component;

import dev.nyom.backend.system.dto.SettingDto;
import dev.nyom.backend.system.model.Setting;
import dev.nyom.backend.system.model.Setting.SettingType;
import dev.nyom.backend.user.model.User;

@Component
public class SettingMapper {
    public SettingDto toDto(Setting setting) {
        SettingDto dto = new SettingDto();
        dto.setKey(setting.getKey());
        dto.setValue(setting.getValue());
        dto.setType(setting.getType());
        return dto;
    }

    public Setting toEntity(SettingDto dto, User user) {
        Setting setting = new Setting();
        setting.setKey(dto.getKey());
        setting.setValue(dto.getValue());
        setting.setType(SettingType.USER);
        setting.setUser(user);
        return setting;
    }

    public Setting toEntity(SettingDto dto) {
        Setting setting = new Setting();
        setting.setKey(dto.getKey());
        setting.setValue(dto.getValue());
        setting.setType(SettingType.SYSTEM);
        return setting;
    }
}
