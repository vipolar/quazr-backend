package io.quazr.backend.Instances.Players.AOF;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import io.quazr.backend.Instances.Players.PlayerCID;

public interface AOFPlayersRepository extends JpaRepository<AOFPlayerEntity, PlayerCID> {
    @Transactional
    @Query(value = """
        SELECT * FROM quizr_upsert_aof_instance_player_characteristics_scores(
            :quizr, :instance, :userUUID, :playful, :calm, :curious, :bold, :gentle, :independent, :loyal, :silly, :adventurous, :graceful, :createdAt, :updatedAt
        );
    """, nativeQuery = true)
    AOFPlayerEntity upsertPlayerScores(
        @Param("quizr") String quizr,
        @Param("instance") UUID instance,
        @Param("userUUID") UUID userUUID,
        @Param("playful") Integer playful,
        @Param("calm") Integer calm,
        @Param("curious") Integer curious,
        @Param("bold") Integer bold,
        @Param("gentle") Integer gentle,
        @Param("independent") Integer independent,
        @Param("loyal") Integer loyal,
        @Param("silly") Integer silly,
        @Param("adventurous") Integer adventurous,
        @Param("graceful") Integer graceful,
        @Param("createdAt") OffsetDateTime createdAt,
        @Param("updatedAt") OffsetDateTime updatedAt
    );

    default AOFPlayerEntity upsertPlayerScores(AOFPlayerEntity answers, UUID invitedBy) {
        return upsertPlayerScores(
            answers.getQuizr(),
            answers.getInstance(),
            answers.getUserUUID(),
            answers.getPlayful(),
            answers.getCalm(),
            answers.getCurious(),
            answers.getBold(),
            answers.getGentle(),
            answers.getIndependent(),
            answers.getLoyal(),
            answers.getSilly(),
            answers.getAdventurous(),
            answers.getGraceful(),
            answers.getCreatedAt(),
            answers.getUpdatedAt()
        );
    };
}
