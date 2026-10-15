package io.quazr.backend.Users;

import org.springframework.web.bind.annotation.RestController;

import io.quazr.backend.Users.Connections.ConnectionsService;
import io.quazr.backend.Users.Connections.ConnectionEntity;
import io.quazr.backend.Users.Settings.UserSettingsService;
import io.quazr.backend.Users.Images.UserImageRecord;

import java.util.Optional;
import java.util.UUID;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "/users")
public class UsersController {
    //SettingDefinitionsService settingDefinitionsService;
    UserSettingsService userSettingsService;
    ConnectionsService connectionsService;
    UsersRepository usersRepository;
    final UsersService usersService;

    private UsersController(UsersRepository usersRepository, ConnectionsService connectionsService, UserSettingsService userSettingsService, UsersService usersService) {
        this.connectionsService = connectionsService;
        this.usersRepository = usersRepository;

        this.userSettingsService = userSettingsService;
        this.usersService = usersService;
    }

    @GetMapping("/{userUUID}")
    public ResponseEntity<?> getUser(@PathVariable(required = true) String userUUID) {
        Optional<UserEntity> userEntity = this.usersRepository.findByUserUUID(UUID.fromString(userUUID));

        if (userEntity.isPresent()) {
            return new ResponseEntity<UserEntity>(userEntity.get(), HttpStatus.OK);
        }

        return new ResponseEntity<String>("none", HttpStatus.OK);
    }

    @GetMapping("/{userUUID}/connections")
    public ResponseEntity<?> getUserFriends(@PathVariable(required = true) UUID userUUID) {
        Page<ConnectionEntity> connections = connectionsService.getAllConnections(userUUID);

        if (connections.isEmpty()) {
            return new ResponseEntity<String>("connection", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Page<ConnectionEntity>>(connections, HttpStatus.OK);
    }

    @PostMapping("/{userUUID}/images/new")
    public ResponseEntity<?> getUserFriends(@ModelAttribute UserImageRecord userImageRecord, @PathVariable(required = true) UUID userUUID) {
        try {
            UUID retval = this.usersService.uploadUserImage(userImageRecord, userUUID);
            return new ResponseEntity<UUID>(retval, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
    }
}
