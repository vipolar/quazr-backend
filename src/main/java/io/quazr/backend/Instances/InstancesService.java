package io.quazr.backend.Instances;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.quazr.backend.Users.UsersService;
import io.quazr.backend.Users.Connections.ConnectionEntity;
import io.quazr.backend.Users.Followers.FollowerEntity;
import io.quazr.backend.Users.Images.UserImagesService;
import io.quazr.backend.Instances.Players.PlayerRecord;
import io.quazr.backend.Instances.Players.PlayerEntity;
import io.quazr.backend.Instances.Players.PlayersRepository;
import io.quazr.backend.Instances.Players.AOF.AOFPlayerEntity;
import io.quazr.backend.Instances.Players.AOF.AOFPlayerRecord;
import io.quazr.backend.Instances.Players.AOF.AOFPlayersRecord;
import io.quazr.backend.Instances.Players.AOF.AOFPlayersRepository;
import io.quazr.backend.Users.UserEntity;

import java.io.IOException;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class InstancesService {
    private final HashMap<InstanceCID, InstanceEntity> instancesCache;
    private final UsersService usersService;
    private final InstancesRepository instancesRepository;
    private final PlayersRepository playersRepository;
    private final UserImagesService userImagesService;
    private final AOFPlayersRepository answersRepository;

    public InstancesService(UsersService usersService, UserImagesService userImagesService, PlayersRepository playersRepository, InstancesRepository instancesRepository, AOFPlayersRepository answersRepository) {
        this.instancesCache = new HashMap<InstanceCID, InstanceEntity>();
        this.userImagesService  = userImagesService;
        this.playersRepository = playersRepository;
        this.usersService = usersService;
        this.instancesRepository = instancesRepository;
        this.answersRepository = answersRepository;
    }

    public InstanceEntity getInstance(String quizrID, UUID instanceUUID) throws NoSuchElementException {
        InstanceCID compositeKey = new InstanceCID().setQuizr(quizrID).setInstance(instanceUUID);
        return this.getInstance(compositeKey);
    }

    private InstanceEntity getInstance(InstanceCID compositeKey) throws NoSuchElementException {
        InstanceEntity instance = this.instancesCache.get(compositeKey);

        if (instance == null) {
            Optional<InstanceEntity> optionalInstance = this.instancesRepository.findById(compositeKey);
            instance = optionalInstance.get();

            if (instance.getConcludedAt() == null) {
                this.instancesCache.put(compositeKey, instance);
            }
        }

        return instance;
    }

    public InstanceEntity createInstance(String quizr, UUID creatorUUID) {
        InstanceEntity newInstance = this.instancesRepository.createInstance(quizr, creatorUUID);
        InstanceCID compositeKey = new InstanceCID().fromInstance(newInstance);
        this.instancesCache.put(compositeKey, newInstance);

        return newInstance;
    }

    public PlayerEntity createPlayer(String quizrID, UUID instanceUUID, String nickname, UUID invitedBy) throws IllegalAccessException {
        InstanceEntity instance = this.getInstance(quizrID, instanceUUID);

        if (!instance.getCreatedBy().equals(invitedBy)) {
            throw new IllegalAccessException("Only the creator of the quiz can add new players to it");
        }

        PlayerEntity newPlayerEntity = new PlayerEntity().setQuizr(quizrID).setInstance(instanceUUID).setNickname(nickname);
        newPlayerEntity = this.playersRepository.upsertPlayer(newPlayerEntity, invitedBy);
        instance.addPlayer(newPlayerEntity);

        return newPlayerEntity;
    }

    public PlayerEntity editPlayer(String quizrID, UUID instanceUUID, PlayerRecord player) {
        InstanceEntity instance = this.getInstance(quizrID, instanceUUID);
        PlayerEntity playerEntity = instance.getPlayer(player.userUUID());
        
        PlayerEntity updatedPlayerEntity = player.toPlayerEntity(playerEntity);
        
        updatedPlayerEntity = this.playersRepository.upsertPlayer(updatedPlayerEntity, instance.getCreatedBy());

        instance.updatePlayer(updatedPlayerEntity);
        return playerEntity;
    }

    public List<AOFPlayerEntity> submitAnswers(String quizrID, UUID instanceUUID, AOFPlayersRecord playersScores) throws IllegalAccessException {
        InstanceEntity instance = this.getInstance(quizrID, instanceUUID);
        List<AOFPlayerEntity> returnList = new ArrayList<AOFPlayerEntity>();

        for (AOFPlayerRecord playerScores : playersScores.players()) {
            AOFPlayerEntity playerScoresEntity = new AOFPlayerEntity()
                                            .setQuizr(instance.getQuizr())
                                            .setInstance(instance.getInstance())
                                            .setUserUUID(playerScores.userUUID())
                                            .fromAnswersRecord(playerScores);

            returnList.add(this.answersRepository.upsertPlayerScores(playerScoresEntity, instance.getCreatedBy()));
        }

        if (returnList.isEmpty()) {
            throw new IllegalArgumentException("NO EMPTY RECORDS!");
        }

        return returnList;
    }
}
