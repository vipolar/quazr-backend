package io.quazr.backend.Users.Images;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.Immutable;
import org.hibernate.generator.EventType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Immutable
@Table(name = "quazr_user_images_wide")
public class UserImageEntity {
    @Id
    @Generated(event = EventType.INSERT)
    @Column(name = "image_uuid", insertable = false, updatable = false)
    private UUID imageUUID;

    @Column(name = "uploader_uuid", updatable = false, nullable = false)
    private UUID uploader;

    @Column(name = "scaled_width", nullable = false)
    private Integer scaledWidth;

    @Column(name = "scaled_height", nullable = false)
    private Integer scaledHeight;

    @Column(name = "angle_in_radians", nullable = false)
    private Double angleInRadians;

    @Column(name = "extract_left", nullable = false)
    private Integer extractLeft;

    @Column(name = "extract_top", nullable = false)
    private Integer extractTop;

    @Column(name = "extract_width", nullable = false)
    private Integer extractWidth;

    @Column(name = "extract_height", nullable = false)
    private Integer extractHeight;

    @Generated(event = EventType.INSERT)
    @Column(name = "uploaded_at", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime uploadedAt;

    @Generated(event = { EventType.INSERT, EventType.UPDATE })
    @Column(name = "updated_at", insertable = false, updatable = false, nullable = false)
    private OffsetDateTime updatedAt;

    public UserImageEntity() {}

    public UserImageEntity fromRecord(UserImageRecord record) {
        if (this.imageUUID != null && !this.imageUUID.equals(record.imageUUID())) {
            throw new IllegalArgumentException("UserImageRecord UUID does not mat UserImageEntity UUID");
        }

        this.scaledWidth = record.scaledWidth();
        this.scaledHeight = record.scaledHeight();
        this.angleInRadians = record.angleInRadians();
        this.extractLeft = record.extractLeft();
        this.extractTop = record.extractTop();
        this.extractWidth = record.extractWidth();
        this.extractHeight = record.extractHeight();

        return this;
    }

    public UUID getImageUUID() {
        return this.imageUUID;
    }

    public UUID getUploader() {
        return this.uploader;
    }

    public UserImageEntity setUploader(UUID user) {
        this.uploader = user;
        return this;
    }

    public Integer getScaledWidth() {
        return this.scaledWidth;
    }

    public UserImageEntity setScaledWidth(Integer scaledWidth) {
        this.scaledWidth = scaledWidth;
        return this;
    }

    public Integer getScaledHeight() {
        return this.scaledHeight;
    }

    public UserImageEntity setScaledHeight(Integer scaledHeight) {
        this.scaledHeight = scaledHeight;
        return this;
    }

    public Double getAngleInRadians() {
        return this.angleInRadians;
    }

    public UserImageEntity setAngleInRadians(Double angleInRadians) {
        this.angleInRadians = angleInRadians;
        return this;
    }
    
    public Integer getExtractLeft() {
        return this.extractLeft;
    }

    public UserImageEntity setExtractLeft(Integer extractLeft) {
        this.extractLeft = extractLeft;
        return this;
    }

    public Integer getExtractTop() {
        return this.extractTop;
    }

    public UserImageEntity setExtractTop(Integer extractTop) {
        this.extractTop = extractTop;
        return this;
    }

    public Integer getExtractWidth() {
        return this.extractWidth;
    }

    public UserImageEntity setExtractWidth(Integer extractWidth) {
        this.extractWidth = extractWidth;
        return this;
    }

    public Integer getExtractHeight() {
        return this.extractHeight;
    }

    public UserImageEntity setExtractHeight(Integer extractHeight) {
        this.extractHeight = extractHeight;
        return this;
    }

    public OffsetDateTime getUploadedAt() {
        return this.uploadedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserImageEntity that = (UserImageEntity) o;
        return this.imageUUID != null && this.imageUUID.equals(that.imageUUID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "UserImageEntity{" +
            "imageUUID=" + imageUUID +
            ", uploader=" + uploader +
            ", scaledWidth=" + scaledWidth +
            ", scaledHeight=" + scaledHeight +
            ", angleInRadians=" + angleInRadians +
            ", extractLeft=" + extractLeft +
            ", extractTop=" + extractTop +
            ", extractWidth=" + extractWidth +
            ", extractHeight=" + extractHeight +
            ", uploadedAt=" + uploadedAt +
            ", updatedAt=" + updatedAt +
        '}';
    }
}