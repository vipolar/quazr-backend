package io.quazr.backend.Users.Connections;

import io.quazr.backend.Users.Connections.CompositeKeys.ConnectionID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.IdClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Immutable
@IdClass(ConnectionID.class)
@Table(name = "quazr_user_connections_view")
public class ConnectionEntity {
    @Id
    @JsonIgnore
    @Column(name = "connection_source_uuid")
    private UUID connectionSourceUUID;

    @Id
    @JsonProperty("userUUID")
    @Column(name = "connection_target_uuid")
    private UUID connectionTargetUUID;

    @JsonProperty("username")
    @Column(name = "connection_target_username")
    private String connectionTargetUsername;

    @JsonProperty("since")
    @Column(name = "connected_since")
    private OffsetDateTime connectedSince;

    @JsonProperty("type")
    @Column(name = "connection_type")
    private String connectionType;

    public UUID getConnectionSourceUUID() {
        return this.connectionSourceUUID;
    }

    public String getConnectionTargetUsername() {
        return this.connectionTargetUsername;
    }

    public UUID getConnectionTargetUUID() {
        return this.connectionTargetUUID;
    }

    public OffsetDateTime getConnectedSince() {
        return this.connectedSince;
    }

    public String getConnectionType() {
        return this.connectionType;
    }
}