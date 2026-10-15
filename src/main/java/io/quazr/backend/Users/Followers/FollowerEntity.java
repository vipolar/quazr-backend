package io.quazr.backend.Users.Followers;

import io.quazr.backend.Users.UserEntity;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.IdClass;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

class FollowerCompositeKey implements Serializable {
    private UUID user;
    private UUID target;

    public FollowerCompositeKey() {
    }

    public FollowerCompositeKey(UUID user, UUID target) {
        this.user = user;
        this.target = target;
    }

    public UUID getUser() {
        return this.user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public UUID getTarget() {
        return this.target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerCompositeKey that = (FollowerCompositeKey) o;
        return Objects.equals(this.user, that.user) && Objects.equals(this.target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.target);
    }
}

@Entity
@Table(name = "quazr_user_connections")
@IdClass(FollowerCompositeKey.class)
public class FollowerEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uuid", nullable = false)
    private UserEntity user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_uuid", nullable = false)
    private UserEntity target;

    @Generated(event = EventType.INSERT)
    @Column(name = "connected_since", insertable = false, updatable = false)
    private OffsetDateTime since;

    public FollowerEntity() {
    }

    public UserEntity getUser() {
        return this.user;
    }

    public FollowerEntity setUser(UserEntity user) {
        this.user = user;
        return this;
    }

    public UserEntity getTarget() {
        return this.target;
    }

    public FollowerEntity setTarget(UserEntity target) {
        this.target = target;
        return this;
    }

    public OffsetDateTime getSince() {
        return this.since;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FollowerEntity that = (FollowerEntity) o;
        return Objects.equals(this.user, that.user) && Objects.equals(this.target, that.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.user, this.target);
    }
}