package io.quazr.backend.Instances.Players.AOF;

import java.util.List;

public record AOFPlayersRecord(List<AOFPlayerRecord> players) {
    public AOFPlayersRecord {
        if (players == null || players.isEmpty()) {
            throw new IllegalArgumentException("Invalid AOFPlayerRecord: no player records present");
        }
    }
}
