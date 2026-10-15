package io.quazr.backend.AdminPanel.Quizes;

import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping(path = "/quizers")
public class QuizController {
    @PostMapping("/new")
    public String postMethodName(@ModelAttribute QuizDTT quiz) {
        //TODO: process POST request

        System.out.println(quiz.getUserUUID().toString());
     /*     quiz.getPlayers().forEach(player -> {
            System.out.println(player.getDescription());
            System.out.println(player.getDescriptor());
            System.out.println(player.getNickname());
            System.out.println(player.getAvatar());
            System.out.println(player.getUser());
        });*/
        return "quiz";
    }
}