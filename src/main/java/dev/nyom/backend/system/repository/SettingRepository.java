package dev.nyom.backend.system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.nyom.backend.system.model.Setting;
import dev.nyom.backend.system.model.Setting.SettingType;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    List<Setting> findByUserIdAndType(Long userId, SettingType type);
    List<Setting> findByType(SettingType type);
    Optional<Setting> findByKeyAndUserIdAndType(String key, Long userId, SettingType type);
    Optional<Setting> findByKeyAndType(String key, SettingType type);
}
