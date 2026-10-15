package io.quazr.backend.Instances;

import java.util.List;
import java.util.UUID;

public record InstanceRecord(UUID instanceUUID, UUID creatorUUID, List<UUID> players) {
    
}
