package io.quazr.backend.Users;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUserUUID(UUID userUUID);

    @Transactional
    @Query(value = """
        SELECT * FROM quazr_upsert_users(
            :username, :userUUID, :invitedBy, :accountUUID, NULL, NULL
        );
    """, nativeQuery = true)
    UserEntity upsertUser(
        @Param("username") String username,
        @Param("userUUID") UUID userUUID,
        @Param("invitedBy") UUID invitedBy,
        @Param("accountUUID") UUID accountUUID
    );
    
    default UserEntity upsertUser(UserEntity entity) {
        return upsertUser(
            entity.getUsername(),
            entity.getUserUUID(),
            entity.getInvitedBy(),
            entity.getAccountUUID()
        );
    }
}
