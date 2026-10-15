package io.quazr.backend.Users.Settings;

import io.quazr.backend.Users.Settings.CompositeKeys.UserSettingID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSetting, UserSettingID> {
    @Query("SELECT s FROM UserSetting s WHERE s.id.userUUID = :userUUID")
    List<UserSetting> findAllExplicitSettings(@Param("userUUID") UUID userUUID);

    @Query("SELECT s FROM UserSetting s WHERE s.id.userUUID = :userUUID AND s.id.settingPath IN :settings")
    List<UserSetting> findExplicitSettings(@Param("userUUID") UUID userUUID, @Param("settings") List<String> settings);
}