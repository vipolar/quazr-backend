package io.quazr.backend.Questions;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/questionnaire")
public class QuestionnaireController {
    private final QuestionnaireRepository questionnaireRepository;

    QuestionnaireController(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    @GetMapping("/{quizrID}")
    public ResponseEntity<?> getMethodName(@PathVariable(required = true) String quizrID) {
        try {
            if (!quizrID.contentEquals("aof")) {
                throw new IllegalArgumentException("not that one: %s".formatted(quizrID));
            }

            Optional<String> rawJson = this.questionnaireRepository.fetchRawJson();
            return new ResponseEntity<String>(rawJson.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Not found", HttpStatus.NOT_FOUND);
        }
    }
}
