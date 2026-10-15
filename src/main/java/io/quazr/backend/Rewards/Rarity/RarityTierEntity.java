package io.quazr.backend.Rewards.Rarity;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.generator.EventType;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;

@Entity
@Table(name = "quazr_rewards_rarity_tiers")
public class RarityTierEntity {
    @Id
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "tier", nullable = false, updatable = false, columnDefinition = "quazr_rewards_rarity_tier")
    private RarityTier tier;

    @Column(name = "description", nullable = false, length = 512)
    private String description;

    @Generated(event = EventType.INSERT)
    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Generated(event = {EventType.INSERT, EventType.UPDATE})
    @Column(name = "updated_at", insertable = false, updatable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "starts_at")
    private OffsetDateTime startsAt;

    @Column(name = "ends_at")
    private OffsetDateTime endsAt;

    public RarityTierEntity() {}

    public RarityTier getTier() {
        return this.tier;
    }

    public RarityTierEntity setTier(RarityTier tier) {
        this.tier = tier;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public RarityTierEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public OffsetDateTime getStartsAt() {
        return this.startsAt;
    }

    public RarityTierEntity setStartsAt(OffsetDateTime startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public OffsetDateTime getEndsAt() {
        return this.endsAt;
    }

    public RarityTierEntity setEndsAt(OffsetDateTime endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RarityTierEntity that = (RarityTierEntity) o;
        return this.tier == that.tier;
    }

    @Override
    public int hashCode() {
        return this.tier != null ? this.tier.hashCode() : getClass().hashCode();
    }
}