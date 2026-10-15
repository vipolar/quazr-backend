package io.quazr.backend.AdminPanel.Quizes;

import io.quazr.backend.Rewards.Rarity.RarityTierEntity;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;

@Entity
@Table(name = "quazr_quizrs")
public class QuizrEntity {
    @Id
    @Column(name = "quizr", nullable = false, updatable = false, length = 32)
    private String quizr;

    @Column(name = "display_name", nullable = false, length = 64)
    private String displayName;

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

    @JsonProperty("rarity")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier", nullable = false)
    private RarityTierEntity rarityTier; 

    public QuizrEntity() {}

    public String getQuizr() {
        return this.quizr;
    }

    public QuizrEntity setQuizr(String quizr) {
        this.quizr = quizr;
        return this;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public QuizrEntity setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public QuizrEntity setDescription(String description) {
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

    public QuizrEntity setStartsAt(OffsetDateTime startsAt) {
        this.startsAt = startsAt;
        return this;
    }

    public OffsetDateTime getEndsAt() {
        return this.endsAt;
    }

    public QuizrEntity setEndsAt(OffsetDateTime endsAt) {
        this.endsAt = endsAt;
        return this;
    }

    public RarityTierEntity getRarityTier() {
        return this.rarityTier;
    }

    public QuizrEntity setRarityTier(RarityTierEntity tier) {
        this.rarityTier = tier;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuizrEntity that = (QuizrEntity) o;
        return this.quizr != null && this.quizr.equals(that.quizr);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}