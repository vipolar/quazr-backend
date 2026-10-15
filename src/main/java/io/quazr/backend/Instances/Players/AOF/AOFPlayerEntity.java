package io.quazr.backend.Instances.Players.AOF;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;

import io.quazr.backend.Instances.Players.PlayerCID;

import jakarta.persistence.IdClass;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@IdClass(PlayerCID.class)
@Table(name = "quizr_aof_instance_players_wide")
public class AOFPlayerEntity {
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
    @Column(name = "user_uuid")
    private UUID userUUID;

    @Column(name = "playful", columnDefinition = "characteristic_score")
    private Integer playful;

    @Column(name = "calm", columnDefinition = "characteristic_score")
    private Integer calm;

    @Column(name = "curious", columnDefinition = "characteristic_score")
    private Integer curious;

    @Column(name = "bold", columnDefinition = "characteristic_score")
    private Integer bold;

    @Column(name = "gentle", columnDefinition = "characteristic_score")
    private Integer gentle;

    @Column(name = "independent", columnDefinition = "characteristic_score")
    private Integer independent;

    @Column(name = "loyal", columnDefinition = "characteristic_score")
    private Integer loyal;

    @Column(name = "silly", columnDefinition = "characteristic_score")
    private Integer silly;

    @Column(name = "adventurous", columnDefinition = "characteristic_score")
    private Integer adventurous;

    @Column(name = "graceful", columnDefinition = "characteristic_score")
    private Integer graceful;

    @Column(name = "matches", columnDefinition = "jsonb")
    private String matches;
    
    @JsonAlias("created_at")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @JsonAlias("updated_at")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public String getQuizr() {
        return this.quizr;
    }

    public AOFPlayerEntity setQuizr(String quizr) {
        this.quizr = quizr;
        return this;
    }

    public UUID getInstance() {
        return this.instance;
    }

    public AOFPlayerEntity setInstance(UUID instance) {
        this.instance = instance;
        return this;
    }

    public UUID getUserUUID() {
        return this.userUUID;
    }

    public AOFPlayerEntity setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
        return this;
    }

    public Integer getPlayful() {
        return this.playful;
    }

    public AOFPlayerEntity setPlayful(Integer playful) {
        this.playful = playful;
        return this;
    }

    public Integer getCalm() {
        return this.calm;
    }

    public AOFPlayerEntity setCalm(Integer calm) {
        this.calm = calm;
        return this;
    }

    public Integer getCurious() {
        return this.curious;
    }

    public AOFPlayerEntity setCurious(Integer curious) {
        this.curious = curious;
        return this;
    }

    public Integer getBold() {
        return this.bold;
    }

    public AOFPlayerEntity setBold(Integer bold) {
        this.bold = bold;
        return this;
    }

    public Integer getGentle() {
        return this.gentle;
    }

    public AOFPlayerEntity setGentle(Integer gentle) {
        this.gentle = gentle;
        return this;
    }

    public Integer getIndependent() {
        return this.independent;
    }

    public AOFPlayerEntity setIndependent(Integer independent) {
        this.independent = independent;
        return this;
    }

    public Integer getLoyal() {
        return this.loyal;
    }

    public AOFPlayerEntity setLoyal(Integer loyal) {
        this.loyal = loyal;
        return this;
    }

    public Integer getSilly() {
        return this.silly;
    }

    public AOFPlayerEntity setSilly(Integer silly) {
        this.silly = silly;
        return this;
    }

    public Integer getAdventurous() {
        return this.adventurous;
    }

    public AOFPlayerEntity setAdventurous(Integer adventurous) {
        this.adventurous = adventurous;
        return this;
    }

    public Integer getGraceful() {
        return this.graceful;
    }

    public AOFPlayerEntity setGraceful(Integer graceful) {
        this.graceful = graceful;
        return this;
    }

    public String getMatches() {
        return this.matches;
    }

    public AOFPlayerEntity setMatches(String matches) {
        this.matches = matches;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public AOFPlayerEntity fromAnswersRecord(AOFPlayerRecord answerRecord) throws IllegalAccessException {
        if (!this.userUUID.equals(answerRecord.userUUID())) {
            throw new IllegalAccessException();
        }

        this.playful = answerRecord.playful();
        this.calm = answerRecord.calm();
        this.curious = answerRecord.curious();
        this.bold = answerRecord.bold();
        this.gentle = answerRecord.gentle();
        this.independent = answerRecord.independent();
        this.loyal = answerRecord.loyal();
        this.silly = answerRecord.silly();
        this.adventurous = answerRecord.adventurous();
        this.graceful = answerRecord.graceful();

        return this;
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
            "quizr='" + quizr + '\'' +
            ", instance=" + instance +
            ", userUUID=" + userUUID +
            ", playful=" + playful +
            ", calm=" + calm +
            ", curious=" + curious +
            ", bold=" + bold +
            ", gentle=" + gentle +
            ", independent=" + independent +
            ", loyal=" + loyal +
            ", silly=" + silly +
            ", adventurous=" + adventurous +
            ", graceful=" + graceful +
            ", matches=" + matches +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.quizr, this.instance, this.userUUID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AOFPlayerEntity)) {
            return false;
        }

        AOFPlayerEntity that = (AOFPlayerEntity) o;
        return Objects.equals(this.quizr, that.quizr)
            && Objects.equals(this.instance, that.instance)
            && Objects.equals(this.userUUID, that.userUUID);
    }
}
