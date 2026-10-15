package io.quazr.backend.Users.Images.Processing;

import java.util.UUID;

public record ProcessorFailure(String task, UUID imageUUID, String outputFile, Integer failureCode, String failureMessage, Object failureCause) {    

}
