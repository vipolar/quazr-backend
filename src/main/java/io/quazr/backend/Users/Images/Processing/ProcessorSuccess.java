package io.quazr.backend.Users.Images.Processing;

import java.util.UUID;

public record ProcessorSuccess(String task, UUID imageUUID, String outputFile) {    
    public ProcessorSuccess {
        if (task == null || imageUUID == null || outputFile == null) {
            throw new IllegalArgumentException("ProcessorSuccess cannot be constructed with arguments missing");
        }
    }
}
