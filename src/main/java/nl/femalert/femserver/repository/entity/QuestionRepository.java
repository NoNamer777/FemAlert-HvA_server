package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.Question;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("QUESTIONS.JPA")
public class QuestionRepository extends AbstractEntityRepository<Question> {

    public QuestionRepository() {
        super(Question.class);
    }
}
