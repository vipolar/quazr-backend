package io.quazr.backend.Users;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.quazr.backend.Users.Images.UserImageRecord;
import io.quazr.backend.Users.Images.UserImagesService;

@Service
public class UsersService {
    private final UserImagesService userImagesService;
    private final UsersRepository usersRepository;
    
    public UsersService(UsersRepository usersRepository, UserImagesService userImagesService) {
        this.userImagesService = userImagesService;
        this.usersRepository = usersRepository;
    }

    public UserEntity createNewUser(String username, UUID invitedBy) {
        UserEntity newUser = this.usersRepository.upsertUser(username, null, invitedBy, null);

        return newUser;
    }

    public UUID uploadUserImage(UserImageRecord imageRecord, UUID uploaderUUID) throws Exception {
        return this.userImagesService.uploadToImageBucket(imageRecord, uploaderUUID);
    }
}
