package io.quazr.backend.Users.Connections;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@Service
public class ConnectionsService {

    private final ConnectionsRepository connectionsRepository;

    public ConnectionsService(ConnectionsRepository connectionsRepository) {
        this.connectionsRepository = connectionsRepository;
    }

    public Page<ConnectionEntity> getAllConnections(UUID userUUID) {
        if (userUUID == null) {
            return null;
        }

        PageRequest pageReq = PageRequest.of(0, 20, Sort.by(Sort.Direction.DESC, "connectedSince"));

        return connectionsRepository.findByConnectionSourceUUID(userUUID, pageReq);
    }
}