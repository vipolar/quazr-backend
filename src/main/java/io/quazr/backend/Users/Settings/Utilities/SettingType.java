package io.quazr.backend.Users.Settings.Utilities;

public enum SettingType {
    INT("int"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    STRING("string"),
    BLANK("blank");

    private final String dbValue;

    SettingType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return this.dbValue;
    }

    public static SettingType fromDbValue(String dbValue) {
        if (dbValue == null) {
            return null;
        }

        for (SettingType type : SettingType.values()) {
            if (type.getDbValue().equals(dbValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown database value for SettingType: %s".formatted(dbValue));
    }
}