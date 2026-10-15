package io.quazr.backend.Users.Settings.Utilities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class SettingTypeConverter implements AttributeConverter<SettingType, String> {

    @Override
    public String convertToDatabaseColumn(SettingType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDbValue();
    }

    @Override
    public SettingType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return SettingType.fromDbValue(dbData);
    }
}