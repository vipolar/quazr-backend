package io.quazr.backend.Rewards.Rarity;

public enum RarityTier {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    EPIC("epic"),
    MYTHICAL("mythical"),
    LEGENDARY("legendary"),
    CELESTIAL("celestial"),
    ETHEREAL("ethereal"),
    TRANSCENDENT("transcendent"),
    EMPYREAN("empyrean");

    private final String dbValue;

    RarityTier(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return this.dbValue;
    }

    public static RarityTier fromDbValue(String dbValue) {
        if (dbValue == null) {
            return null;
        }

        for (RarityTier type : RarityTier.values()) {
            if (type.getDbValue().equals(dbValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown database value for RarityTier: %s".formatted(dbValue));
    }
}