package io.quazr.backend.Instances.Players;

import java.util.UUID;

import org.springframework.stereotype.Service;

import io.quazr.backend.Instances.InstanceEntity;

@Service
public class PlayersService {
    private final PlayersRepository playersRepository;

    public PlayersService(PlayersRepository playersRepository) {
        this.playersRepository = playersRepository;
    }

    public PlayerEntity addPlayer(InstanceEntity instance, PlayerRecord player) {
        PlayerEntity newPlayerEntity = new PlayerEntity()
            .setQuizr(instance.getQuizr())
            .setInstance(instance.getInstance())
            .setUserUUID(player.userUUID())
            .setNickname(player.nickname())
            .setDescriptor(player.descriptor())
            .setDescription(player.description());

        if (player.image() != null) {
            //TODO: all the image checks
            newPlayerEntity.setImageUUID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }

        if (player.avatar() != null) {
            //TODO: all the image checks
            newPlayerEntity.setAvatarUUID(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        }

        System.out.println(player.toString());

        if (newPlayerEntity != null) {
            this.playersRepository.save(newPlayerEntity);
        }
        return newPlayerEntity;
    }
}
