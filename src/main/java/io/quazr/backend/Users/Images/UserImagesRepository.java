package io.quazr.backend.Users.Images;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserImagesRepository extends JpaRepository<UserImageEntity, UUID> {
    @Transactional
    @Query(value = """
        SELECT * FROM quazr_upsert_user_images(
            :imageUUID, :uploaderUUID, :scaledWidth, :scaledHeight, :angleInRadians, :extractLeft, :extractTop, :extractWidth, :extractHeight, NULL, NULL
        );
    """, nativeQuery = true)
    UserImageEntity upsertUserImage(
        @Param("imageUUID") UUID imageUUID,
        @Param("uploaderUUID") UUID uploader,
        @Param("scaledWidth") Integer scaledWidth,
        @Param("scaledHeight") Integer scaledHeight,
        @Param("angleInRadians") Double angleInRadians,
        @Param("extractLeft") Integer extractLeft,
        @Param("extractTop") Integer extractTop,
        @Param("extractWidth") Integer extractWidth,
        @Param("extractHeight") Integer extractHeight
    );

    default UserImageEntity upsertUserImage(UserImageEntity entity) {
        return upsertUserImage(
            entity.getImageUUID(),
            entity.getUploader(),
            entity.getScaledWidth(),
            entity.getScaledHeight(),
            entity.getAngleInRadians(),
            entity.getExtractLeft(),
            entity.getExtractTop(),
            entity.getExtractWidth(),
            entity.getExtractHeight()
        );
    }
}
