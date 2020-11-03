package nl.femalert.femserver.model.entity;

import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "[question]")
@NamedQueries({
    @NamedQuery(name = "find_all_question", query = "select Q from Question Q"),
    @NamedQuery(name = "find_question_by_id", query = "select Q from Question Q where Q.id = ?1"),
    @NamedQuery(name = "find_question_by_question", query = "select Q from Question Q where Q.question = ?1"),
    @NamedQuery(name = "find_question_by_answer", query = "select Q from Question Q where Q.answer = ?1")
})
public class Question implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_question")
    @GenericGenerator(
        name = "_seq_question",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "FAQ-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d")
    })
    private String id;

    @Column(name = "question", nullable = false, unique = true)
    private String question;

    @Column(name = "answer", nullable = false)
    private String answer;

    protected Question() {}

    public Question(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
