package io.quazr.backend.Users.Settings;

import io.quazr.backend.Users.Settings.CompositeKeys.UserSettingID;
import io.quazr.backend.Users.UserEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "quazr_user_settings")
public class UserSetting {
	@EmbeddedId
	private UserSettingID id;

	@MapsId("userUUID")
	@JoinColumn(name = "user_uuid")
	@ManyToOne(fetch = FetchType.LAZY)
	private UserEntity user;

	@MapsId("settingPath")
	@JoinColumn(name = "setting_path")
	@ManyToOne(fetch = FetchType.LAZY)
	private SettingDefinition definition;

	@Column(name = "value")
	private String value;

	public UserSetting() {}

	public UserSetting(UserEntity user, SettingDefinition definition, String value) {
		this.definition = definition;
		this.value = value;
		this.user = user;

		if (user != null && definition != null) {
			this.id = new UserSettingID(user.getUserUUID(), definition.getSettingPath());
		}
	}

	public UserSettingID getId() {
		return this.id;
	}

	public UserSetting setId(UserSettingID id) {
		this.id = id;
		return this;
	}

	public UserEntity getUser() {
		return this.user;
	}

	public UserSetting setUser(UserEntity user) {
		if (this.id == null) {
			this.id = new UserSettingID();
		}

		this.id.setUserUUID(user.getUserUUID());
		this.user = user;
		return this;
	}

	public SettingDefinition getDefinition() {
		return this.definition;
	}

	public UserSetting setDefinition(SettingDefinition definition) {
		if (this.id == null) {
			this.id = new UserSettingID();
		}

		this.id.setSettingPath(definition.getSettingPath());
		this.definition = definition;
		return this;
	}

	public String getValue() {
		return this.value;
	}

	public UserSetting setValue(String value) {
		this.value = value;
		return this;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSetting that = (UserSetting) o;
        return this.id != null && this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}