package io.quazr.backend.Instances;

import io.quazr.backend.Instances.Players.PlayerEntity;
import io.quazr.backend.Instances.Players.PlayerRecord;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.IdClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.UUID;

@Entity
@Immutable
@IdClass(InstanceCID.class)
@Table(name = "quazr_quizr_instances_wide")
public class InstanceEntity {
    @Id
    @Column(name = "quizr", nullable = false)
    private String quizr;

    @Id
    @Column(name = "instance", nullable = false)
    private UUID instance;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_by", updatable = false, nullable = false)
    private UUID createdBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "players", nullable = false)
    private List<PlayerEntity> players;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "concluded_at", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime concludedAt;

    public String getQuizr() {
        return this.quizr;
    }

    public InstanceEntity setQuizr(String quizr) {
        this.quizr = quizr;
        return this;
    }

    public UUID getInstance() {
        return this.instance;
    }

    public InstanceEntity setInstance(UUID instance) {
        this.instance = instance;
        return this;
    }

    public UUID getCreatedBy() {
        return this.createdBy;
    }

    public InstanceEntity setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public List<PlayerEntity> getPlayers() {
        return this.players;
    }

    public InstanceEntity setPlayers(List<PlayerEntity> players) {
        this.players = players;
        return this;
    }

    public PlayerEntity getPlayer(UUID playerUUID) {
        if (this.players == null) {
            return null;
        }

        for (PlayerEntity player : this.players) {
            if (player != null && player.getUserUUID() != null && player.getUserUUID().equals(playerUUID)) {
                return player;
            }
        }

        return null;
    }

    public InstanceEntity updatePlayer(PlayerEntity updatedPlayer) {
        if (this.players == null) {
            throw new NoSuchElementException("No players in %s.%s instance".formatted(this.quizr, this.instance.toString()));
        }

        ListIterator<PlayerEntity> PlayersIterator = this.players.listIterator();
        while (PlayersIterator.hasNext()) {
            PlayerEntity player = PlayersIterator.next();

            if (player.getUserUUID().equals(updatedPlayer.getUserUUID())) {
                PlayersIterator.set(player);
                return this;
            }
        }

        throw new NoSuchElementException("No player '%s' in %s.%s instance".formatted(updatedPlayer.getUserUUID().toString(), this.quizr, this.instance.toString()));
    }

    public InstanceEntity addPlayer(PlayerEntity newPlayer) {
        if (this.players == null) {
            throw new NoSuchElementException("No players in %s.%s instance".formatted(this.quizr, this.instance.toString()));
        };

        this.players.add(newPlayer);
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public OffsetDateTime getConcludedAt() {
        return this.concludedAt;
    }
}
