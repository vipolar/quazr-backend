package io.quazr.backend.Instances.Players.AOF;

import java.util.UUID;

public record AOFPlayerRecord(UUID userUUID, Integer playful, Integer calm, Integer curious, Integer bold, Integer gentle, Integer independent, Integer loyal, Integer silly, Integer adventurous, Integer graceful) {
    private static void validateCharacteristicScore(Integer value, String name) {
        if (value == null || value < 1 || value > 3) {
            throw new IllegalArgumentException("Invalid AOFPlayerRecord: characteristic '%s' score must be between 1 and 3 (inclusive)".formatted(name));
        }
    }

    public AOFPlayerRecord {
        if (userUUID == null) {
            throw new IllegalArgumentException("Invalid AOFPlayerRecord: no user UUID present");
        }

        validateCharacteristicScore(independent, "independent");
        validateCharacteristicScore(adventurous, "adventurous");
        validateCharacteristicScore(graceful, "graceful");
        validateCharacteristicScore(playful, "playful");
        validateCharacteristicScore(curious, "curious");
        validateCharacteristicScore(gentle, "gentle");
        validateCharacteristicScore(loyal, "loyal");
        validateCharacteristicScore(silly, "silly");
        validateCharacteristicScore(bold, "bold");
        validateCharacteristicScore(calm, "calm");
    }
}
