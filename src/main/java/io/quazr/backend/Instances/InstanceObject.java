package io.quazr.backend.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.quazr.backend.Instances.Players.PlayerEntity;

public class InstanceObject {
    private List<PlayerEntity> players;
    private InstanceEntity instance;

    public InstanceObject() {
        players = new ArrayList<PlayerEntity>();
    }
    
    public InstanceEntity getInstance() {
        return this.instance;
    }

    public InstanceObject setInstance(InstanceEntity instance) {
        this.instance = instance;
        return this;
    }

    public List<PlayerEntity> getPlayers() {
        return this.players;
    }

    public InstanceObject setPlayers(List<PlayerEntity> players) {
        this.players = players;
        return this;
    }

    public PlayerEntity getPlayer(UUID userUUID) {
        for (PlayerEntity player : this.players) {
            if (player.getUserUUID().equals(userUUID)) {
                return player;
            }
        }

        return null;
    }

    public InstanceObject addPlayer(PlayerEntity player) {
        this.players.add(player);
        return this;
    }
}
