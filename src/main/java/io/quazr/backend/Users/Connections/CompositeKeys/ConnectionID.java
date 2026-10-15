package io.quazr.backend.Users.Connections.CompositeKeys;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class ConnectionID implements Serializable {
    private UUID connectionSourceUUID;
    private UUID connectionTargetUUID;

    public ConnectionID() {}

    public ConnectionID(UUID connectionSourceUUID, UUID connectionTargetUUID) {
        this.connectionSourceUUID = connectionSourceUUID;
        this.connectionTargetUUID = connectionTargetUUID;
    }

    public UUID getconnectionSourceUUID() {
        return this.connectionSourceUUID;
    }

    public ConnectionID setconnectionSourceUUID(UUID connectionSourceUUID) {
        this.connectionSourceUUID = connectionSourceUUID;
        return this;
    }

    public UUID getconnectionTargetUUID() {
        return this.connectionTargetUUID;
    }

    public ConnectionID setconnectionTargetUUID(UUID connectionTargetUUID) {
        this.connectionTargetUUID = connectionTargetUUID;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionID that = (ConnectionID) o;
        return Objects.equals(this.connectionSourceUUID, that.connectionSourceUUID) &&
               Objects.equals(this.connectionTargetUUID, that.connectionTargetUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.connectionSourceUUID, this.connectionTargetUUID);
    }
}