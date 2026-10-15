package io.quazr.backend.Users.Images;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UserImageRecord(MultipartFile image, UUID imageUUID, Integer scaledWidth, Integer scaledHeight, Double angleInRadians, Integer extractLeft, Integer extractTop, Integer extractWidth, Integer extractHeight) {
    public UserImageRecord {
        if ((image == null && imageUUID == null) || scaledWidth == null || scaledHeight == null || angleInRadians == null || extractLeft == null || extractTop == null || extractWidth == null || extractHeight == null) {
            throw new IllegalArgumentException("UserImageRecord cannot be constructed with arguments missing");
        };
    };
};
