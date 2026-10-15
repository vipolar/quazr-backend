package io.quazr.backend.Questions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Immutable
@Table(name = "quizr_aof_questionnaire_json")
public class QuestionnaireEntity {
    @Id
    @Column(name = "id")
    private Integer id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "questionnaire", columnDefinition = "jsonb")
    private String questionnaire;

    public Integer getId() {
        return id;
    }

    public String getQuestionnaire() {
        return questionnaire;
    }
}