package io.quazr.backend.Instances;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class InstanceCID implements Serializable {
    @Column(name = "quizr", nullable = false)
    private String quizr;

    @Column(name = "instance", nullable = false)
    private UUID instance;

    public InstanceCID() {}

    public String getQuizr() {
        return this.quizr;
    }

    public InstanceCID setQuizr(String quizr) {
        this.quizr = quizr;
        return this;
    }

    public UUID getInstance() {
        return this.instance;
    }

    public InstanceCID setInstance(UUID instance) {
        this.instance = instance;
        return this;
    }

    public InstanceCID fromInstance(InstanceEntity instance) {
        this.instance = instance.getInstance();
        this.quizr = instance.getQuizr();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        InstanceCID that = (InstanceCID) o;
        return Objects.equals(this.quizr, that.quizr) &&
                Objects.equals(this.instance, that.instance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.quizr, this.instance);
    }
}
