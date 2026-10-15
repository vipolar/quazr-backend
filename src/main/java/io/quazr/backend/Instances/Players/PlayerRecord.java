package io.quazr.backend.Instances.Players;

import java.util.UUID;

public record PlayerRecord(UUID userUUID, String nickname, UUID image, UUID avatar, String descriptor, String description) {
    public PlayerRecord {
        nickname = nickname != null ? nickname.trim() : null;
        descriptor = descriptor != null ? descriptor.trim() : null;
        description = description != null ? description.trim() : null;

        if (userUUID == null) {
            throw new IllegalArgumentException("user UUID cannot be null");
        }

        if (nickname != null && (nickname.isBlank() || nickname.length() > 32)) {
            System.out.println(nickname);
            throw new IllegalArgumentException("nickname must be 32 characters or less but it cannot be blank");
        }

        if (descriptor != null && (descriptor.isBlank() || descriptor.length() > 64)) {
            System.out.println(descriptor);
            throw new IllegalArgumentException("descriptor must be 64 characters or less but it cannot be blank");
        }

        if (description != null && (description.isBlank() || description.length() > 255)) {
            throw new IllegalArgumentException("description must be 255 characters or less but it cannot be blank");
        }
    }

    public PlayerEntity toPlayerEntity(PlayerEntity playerEntity) {
        if (playerEntity == null || playerEntity.getUserUUID() == null || !playerEntity.getUserUUID().equals(this.userUUID)) {
            throw new IllegalArgumentException("player entity must match the user UUID of the player record");
        }

        playerEntity.setImageUUID(this.image);
        playerEntity.setAvatarUUID(this.avatar);
        playerEntity.setNickname(this.nickname);
        playerEntity.setDescriptor(this.descriptor);
        playerEntity.setDescription(this.description);
        
        return playerEntity;
    }

    public PlayerEntity toNewPlayerEntity() {
        return new PlayerEntity()
            .setImageUUID(this.image)
            .setAvatarUUID(this.avatar)
            .setUserUUID(this.userUUID)
            .setNickname(this.nickname)
            .setDescriptor(this.descriptor);
    }
}
