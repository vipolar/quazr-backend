package io.quazr.backend.Users;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "quazr_users")
public class UserEntity {
    @Id
    @Generated(event = EventType.INSERT)
    @Column(name = "user_uuid", updatable = false, nullable = false)
    private UUID userUUID;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "invited_by", updatable = false, nullable = false)
    private UUID invitedBy;

    @Column(name = "account_uuid", nullable = false)
    private UUID accountUUID;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime updatedAt;

    public UserEntity() {}

    public UUID getUserUUID() {
        return this.userUUID;
    }

    public UserEntity setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
        return this;
    }

    public String getUsername() {
        return this.username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public UUID getInvitedBy() {
        return this.invitedBy;
    }

    public UserEntity setInvitedBy(UUID invitedBy) {
        this.invitedBy = invitedBy;
        return this;
    }

    public UUID getAccountUUID() {
        return this.accountUUID;
    }

    public UserEntity setAccountUUID(UUID accountUUID) {
        this.accountUUID = accountUUID;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return this.userUUID != null && this.userUUID.equals(that.userUUID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
