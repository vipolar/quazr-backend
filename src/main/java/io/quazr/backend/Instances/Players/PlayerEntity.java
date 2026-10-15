package io.quazr.backend.Instances.Players;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.IdClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Immutable
@IdClass(PlayerCID.class)
@Table(name = "quazr_quizr_instance_players")
public class PlayerEntity {
    @Id
    @Column(name = "quizr")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String quizr;

    @Id
    @Column(name = "instance")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UUID instance;

    @Id
    @JsonAlias("user_uuid")
    @Generated(event = EventType.INSERT)
    @Column(name = "user_uuid")
    private UUID userUUID;

    @JsonAlias("avatar_uuid")
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "avatar_uuid")
    private UUID avatarUUID;

    @JsonAlias("image_uuid")
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "image_uuid")
    private UUID imageUUID;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "descriptor")
    private String descriptor;

    @Column(name = "description")
    private String description;

    @JsonAlias("created_at")
    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @JsonAlias("updated_at")
    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    public String getQuizr() {
        return this.quizr;
    }

    public PlayerEntity setQuizr(String quizr) {
        this.quizr = quizr;
        return this;
    }

    public UUID getInstance() {
        return this.instance;
    }

    public PlayerEntity setInstance(UUID instance) {
        this.instance = instance;
        return this;
    }

    public UUID getUserUUID() {
        return this.userUUID;
    }

    public PlayerEntity setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
        return this;
    }

    public UUID getAvatarUUID() {
        return this.avatarUUID;
    }

    public PlayerEntity setAvatarUUID(UUID avatarUUID) {
        this.avatarUUID = avatarUUID;
        return this;
    }

    public UUID getImageUUID() {
        return this.imageUUID;
    }

    public PlayerEntity setImageUUID(UUID imageUUID) {
        this.imageUUID = imageUUID;
        return this;
    }

    public String getNickname() {
        return this.nickname;
    }

    public PlayerEntity setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public PlayerEntity setDescriptor(String descriptor) {
        this.descriptor = descriptor;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public PlayerEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }
}
