package io.quazr.backend.Users.Connections;

import io.quazr.backend.Users.Connections.CompositeKeys.ConnectionID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.UUID;

@Repository
public interface ConnectionsRepository extends JpaRepository<ConnectionEntity, ConnectionID> {
    Page<ConnectionEntity> findByConnectionSourceUUID(UUID connectionSourceUUID, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO quazr_user_connections (user_uuid, target_uuid) 
        VALUES (:userUUID, :targetUUID)
        ON CONFLICT (user_uuid, target_uuid) DO NOTHING
    """, nativeQuery = true)
    void createConnection(@Param("userUUID") UUID userUUID, @Param("targetUUID") UUID targetUUID);

    @Modifying
    @Transactional
    @Query(value = """
        DELETE FROM quazr_user_connections 
        WHERE user_uuid = :userUUID AND target_uuid = :targetUUID
    """, nativeQuery = true)
    void removeConnection(@Param("userUUID") UUID userUUID, @Param("targetUUID") UUID targetUUID);
}