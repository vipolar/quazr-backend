package io.quazr.backend.Instances.Players;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

public interface PlayersRepository extends JpaRepository<PlayerEntity, PlayerCID> {
    @Transactional
    @Query(value = """
        SELECT * FROM quazr_upsert_quizr_instance_players(
            :quizr, :instance, :userUUID, :invitedBy, :avatar, :image, :nickname, :descriptor, :description, :createdAt, :updatedAt
        );
    """, nativeQuery = true)
    PlayerEntity upsertPlayer(
        @Param("quizr") String quizr,
        @Param("instance") UUID instance,
        @Param("userUUID") UUID userUUID,
        @Param("invitedBy") UUID invitedBy,
        @Param("avatar") UUID avatar,
        @Param("image") UUID image,
        @Param("nickname") String nickname,
        @Param("descriptor") String descriptor,
        @Param("description") String description,
        @Param("createdAt") OffsetDateTime createdAt,
        @Param("updatedAt") OffsetDateTime updatedAt
    );

    default PlayerEntity upsertPlayer(PlayerEntity player, UUID invitedBy) {
        return upsertPlayer(
            player.getQuizr(),
            player.getInstance(),
            player.getUserUUID(),
            invitedBy,
            player.getAvatarUUID(),
            player.getImageUUID(),
            player.getNickname(),
            player.getDescriptor(),
            player.getDescription(),
            player.getCreatedAt(),
            player.getUpdatedAt()
        );
    };
}
