package io.quazr.backend.Questions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface QuestionnaireRepository extends JpaRepository<QuestionnaireEntity, Integer> {

    // Option A: Explicit database primary lookup
    default Optional<QuestionnaireEntity> fetchQuestionnaire() {
        return findById(1);
    }

    // Option B: Direct native query lookup stripping the wrapper row completely
    @Query(value = "SELECT questionnaire FROM quizr_aof_questionnaire_json LIMIT 1", nativeQuery = true)
    Optional<String> fetchRawJson();
}