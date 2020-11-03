package nl.femalert.femserver.controller.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.entity.Question;
import nl.femalert.femserver.model.helper.exception.EntityAlreadyExistsException;
import nl.femalert.femserver.model.helper.exception.EntityIdConflictException;
import nl.femalert.femserver.model.helper.exception.EntityNotFoundException;
import nl.femalert.femserver.repository.generic.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private EntityRepository<Question> questionRepo;

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepo.findAll();
    }

    @GetMapping("/{id}")
    public Question getQuestionById(@PathVariable String id) {
        Question foundQuestion = questionRepo.findById(id);

        if (foundQuestion == null) throw new EntityNotFoundException(
            String.format("Question with ID: '%s' is not found.", id)
        );
        return foundQuestion;
    }

    @PostMapping
    public ResponseEntity<Question> saveQuestion(@RequestBody ObjectNode questionData) {
        Question question = getQuestionData(questionData);
        List<Question> foundQuestions =
            questionRepo.findByQuery("find_question_by_question", question.getQuestion());

        if (!foundQuestions.isEmpty()) throw new EntityAlreadyExistsException(
            String.format("There already exists an question asked: '%s'", question.getQuestion())
        );
        question = questionRepo.save(question);

        return ResponseEntity.ok(question);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@RequestBody ObjectNode questionData, @PathVariable String id) {
        Question newQuestionData = getQuestionData(questionData);
        Question oldQuestion = getQuestionById(id);

        if (!newQuestionData.getId().equals(oldQuestion.getId())) {
            throw new EntityIdConflictException(String.format(
                "Provided Question ID: '%s' is not the same as the ID of the Question ID in the request body: '%s'",
                newQuestionData.getId(),
                oldQuestion.getId()
            ));
        }
        newQuestionData = questionRepo.save(newQuestionData);

        return ResponseEntity.ok(newQuestionData);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable String id) {
        Question foundQuestion = getQuestionById(id);

        questionRepo.deleteById(foundQuestion.getId());
    }

    private Question getQuestionData(ObjectNode questionData) {
        String id = questionData.get("id") == null ? null : questionData.get("id").asText();
        String asked = questionData.get("question") == null ? null : questionData.get("question").asText();
        String answer = questionData.get("answer") == null ? null : questionData.get("answer").asText();

        Question question = new Question(id);
        question.setQuestion(asked);
        question.setAnswer(answer);

        return question;
    }
}
