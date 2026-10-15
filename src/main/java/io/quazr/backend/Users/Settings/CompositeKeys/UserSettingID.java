package io.quazr.backend.Users.Settings.CompositeKeys;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class UserSettingID implements Serializable {
    @Column(name = "user_uuid")
    private UUID userUUID;

    @Column(name = "setting_path")
    private String settingPath;

    public UserSettingID() {}

    public UserSettingID(UUID userUUID, String settingPath) {
        this.settingPath = settingPath;
		this.userUUID = userUUID;
    }

    public UUID getUserUUID() {
        return this.userUUID;
    }

    public UserSettingID setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
		return this;
    }

    public String getSettingPath() {
        return this.settingPath;
    }

    public UserSettingID setSettingPath(String settingPath) {
        this.settingPath = settingPath;
		return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserSettingID that = (UserSettingID) o;
        return Objects.equals(this.userUUID, that.userUUID) &&
                Objects.equals(this.settingPath, that.settingPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.userUUID, this.settingPath);
    }
}