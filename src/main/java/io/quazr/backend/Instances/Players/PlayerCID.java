package io.quazr.backend.Instances.Players;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PlayerCID implements Serializable {
    @Column(name = "quizr")
    private String quizr;

    @Column(name = "instance")
    private UUID instance;
    
    @Column(name = "user_uuid")
    private UUID userUUID;
    
    public PlayerCID() {}
    
    public String getQuizr() {
        return quizr;
    }
    
    public PlayerCID setQuizr(String quizr) {
        this.quizr = quizr;
        return this;
    }

    public UUID getInstance() {
        return instance;
    }

    public PlayerCID setInstance(UUID instance) {
        this.instance = instance;
        return this;
    }

    public UUID getUserUUID() {
        return userUUID;
    }

    public PlayerCID setUserUUID(UUID userUUID) {
        this.userUUID = userUUID;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PlayerCID that = (PlayerCID) o;
        return Objects.equals(this.quizr, that.quizr) &&
                Objects.equals(this.instance, that.instance) &&
                Objects.equals(this.userUUID, that.userUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.quizr, this.instance, this.userUUID);
    }
}
