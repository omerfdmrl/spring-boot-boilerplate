package dev.nyom.backend.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import dev.nyom.backend.exceptions.ErrorCodes;
import dev.nyom.backend.exceptions.GlobalException;
import dev.nyom.backend.system.model.Setting;
import dev.nyom.backend.system.model.Setting.SettingType;
import dev.nyom.backend.system.repository.SettingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SettingService {
    private final SettingRepository settingRepository;

    public List<Setting> getUserSettings(Long userId) {
        return settingRepository.findByUserIdAndType(userId, SettingType.USER);
    }

    public List<Setting> getSystemSettings() {
        return settingRepository.findByType(SettingType.SYSTEM);
    }

    public Setting saveSetting(Setting setting) {
        return settingRepository.save(setting);
    }

    public void deleteSetting(String key, Long userId) {
        Setting setting = settingRepository.findByKeyAndUserIdAndType(key, userId, SettingType.USER)
            .orElseThrow(() -> new GlobalException(ErrorCodes.SETTING_NOT_FOUND));
        settingRepository.delete(setting);
    }

    public void updateUserSetting(Long userId, String key, String value) {
        Setting setting = settingRepository
            .findByKeyAndUserIdAndType(key, userId, SettingType.USER)
            .orElseThrow(() -> new GlobalException(ErrorCodes.SETTING_NOT_FOUND));

        setting.setValue(value);
        settingRepository.save(setting);
    }

    public void updateSystemSetting(String key, String value) {
        Setting setting = settingRepository
            .findByKeyAndType(key, SettingType.SYSTEM)
            .orElseThrow(() -> new GlobalException(ErrorCodes.SETTING_NOT_FOUND));

        setting.setValue(value);
        settingRepository.save(setting);
    }
}
