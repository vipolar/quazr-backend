package io.quazr.backend.Users.Settings;

import io.quazr.backend.Users.Settings.Utilities.SettingTypeConverter;
import io.quazr.backend.Users.Settings.Utilities.SettingType;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.util.List;

@Entity
@Table(name = "quazr_user_setting_definitions")
public class SettingDefinition {
    @Id
    @Column(name = "setting_path", nullable = false, updatable = false)
    private String settingPath;

    @Convert(converter = SettingTypeConverter.class)
    @Column(name = "setting_type", nullable = false, columnDefinition = "quazr_user_setting_type")
    private SettingType settingType;

    @Column(name = "display_title", nullable = false)
    private String displayTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enabling_setting")
    private SettingDefinition enablingSetting;

    @Column(name = "enabling_value")
    private String enablingValue;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "min_value")
    private String minValue;

    @Column(name = "max_value")
    private String maxValue;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "options", columnDefinition = "varchar[]")
    private List<String> options;

    @Column(name = "description", length = 512)
    private String description;

    public SettingDefinition() {}

    public String getSettingPath() {
        return this.settingPath;
    }

    public SettingDefinition setSettingPath(String settingPath) {
        this.settingPath = settingPath;
        return this;
    }

    public SettingType getSettingType() {
        return this.settingType;
    }

    public SettingDefinition setSettingType(SettingType settingType) {
        this.settingType = settingType;
        return this;
    }

    public String getDisplayTitle() {
        return this.displayTitle;
    }

    public SettingDefinition setDisplayTitle(String displayTitle) {
        this.displayTitle = displayTitle;
        return this;
    }

    public SettingDefinition getEnablingSetting() {
        return this.enablingSetting;
    }

    public SettingDefinition setEnablingSetting(SettingDefinition enablingSetting) {
        this.enablingSetting = enablingSetting;
        return this;
    }

    public String getEnablingValue() {
        return this.enablingValue;
    }

    public SettingDefinition setEnablingValue(String enablingValue) {
        this.enablingValue = enablingValue;
        return this;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public SettingDefinition setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public String getMinValue() {
        return this.minValue;
    }

    public SettingDefinition setMinValue(String minValue) {
        this.minValue = minValue;
        return this;
    }

    public String getMaxValue() {
        return this.maxValue;
    }

    public SettingDefinition setMaxValue(String maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    public List<String> getOptions() {
        return this.options;
    }

    public SettingDefinition setOptions(List<String> options) {
        this.options = options;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public SettingDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SettingDefinition that = (SettingDefinition) o;
        return this.settingPath != null && this.settingPath.equals(that.settingPath);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}