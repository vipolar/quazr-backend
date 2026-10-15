package io.quazr.backend.Users.Images;

import io.quazr.backend.Users.Images.Processing.ProcessingService;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserImagesService {
    private final UserImagesRepository userImagesRepository;
    private final ProcessingService userImagesProcessor;
 
    public UserImagesService(UserImagesRepository userImagesRepository, ProcessingService userImagesProcessor) {
        this.userImagesRepository = userImagesRepository;
        this.userImagesProcessor = userImagesProcessor;
        /* we also need to get delete and stuff like that in here */
    }

    public UUID uploadToImageBucket(UserImageRecord imageRecord, UUID uploaderUUID) throws Exception {
        UserImageEntity imageEntity = new UserImageEntity().fromRecord(imageRecord).setUploader(uploaderUUID);
        imageEntity = this.userImagesRepository.upsertUserImage(imageEntity);

        /* here we do all of the db stuff and if that succeeds then we send it to processing */
        this.userImagesProcessor.processImage(imageRecord.image(), imageEntity);

        
        return imageEntity.getImageUUID();
    } 
}