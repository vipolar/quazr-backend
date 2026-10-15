package io.quazr.backend.Instances;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import io.quazr.backend.Instances.Players.AOF.AOFPlayersRecord;
import io.quazr.backend.Instances.Players.AOF.AOFPlayerEntity;
import io.quazr.backend.Instances.Players.PlayerRecord;
import io.quazr.backend.Instances.Players.PlayerEntity;

import io.quazr.backend.Security.SecurityService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping(path = "/instances")
public class InstancesController {
    private InstancesService instancesService;
    private SecurityService securityService;

    private InstancesController(SecurityService securityService, InstancesService instancesService) {
        this.instancesService = instancesService;
        this.securityService = securityService;
    }

    @PostMapping("/{quizrID}/new")
    public ResponseEntity<?> newInstance(@PathVariable(required = true) String quizrID) {
        try {
            final UUID creatorUUID = this.securityService.extractUserUUID();
            final Map<String, Object> response = new HashMap<>();

            InstanceEntity instance = this.instancesService.createInstance(quizrID, creatorUUID);
            response.put("instance", instance.getInstance());

            if (creatorUUID == null) {
                response.put("token", this.securityService.generateTemporaryToken(instance.getCreatedBy()));
            }

            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Need to fix this stuff", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{quizrID}/{instanceUUID}")
    public ResponseEntity<?> getInstance(@PathVariable(required = true) String quizrID, @PathVariable(required = true) UUID instanceUUID) {
        try {
            InstanceEntity instanceEntity = this.instancesService.getInstance(quizrID, instanceUUID);
            return new ResponseEntity<InstanceEntity>(instanceEntity, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{quizrID}/{instanceUUID}/player/create")
    public ResponseEntity<?> createPlayer(@PathVariable(required = true) String quizrID, @PathVariable(required = true) UUID instanceUUID, @RequestParam(required = true) String nickname){
        try {
            final UUID creatorUUID = this.securityService.extractUserUUID();
            System.out.println(creatorUUID.toString());
            final PlayerEntity newPlayerEntity = this.instancesService.createPlayer(quizrID, instanceUUID, nickname, creatorUUID);

            return new ResponseEntity<UUID>(newPlayerEntity.getUserUUID(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Need to fix this stuff", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{quizrID}/{instanceUUID}/player/edit")
    public ResponseEntity<?> newPlayer(@PathVariable(required = true) String quizrID, @PathVariable(required = true) UUID instanceUUID, @ModelAttribute PlayerRecord playerRecord){
        try {
            PlayerEntity newPlayerEntity = this.instancesService.editPlayer(quizrID, instanceUUID, playerRecord);
            return new ResponseEntity<UUID>(newPlayerEntity.getUserUUID(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Need to fix this stuff", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{quizrID}/{instanceUUID}/answers")
    public ResponseEntity<?> answers(@PathVariable(required = true) String quizrID, @PathVariable(required = true) UUID instanceUUID, @ModelAttribute AOFPlayersRecord answersRecord){
        try {
            List<AOFPlayerEntity> returnList = this.instancesService.submitAnswers(quizrID, instanceUUID, answersRecord);
            return new ResponseEntity<List<AOFPlayerEntity>>(returnList, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>("Need to fix this stuff", HttpStatus.BAD_REQUEST);
        }
    }
}
