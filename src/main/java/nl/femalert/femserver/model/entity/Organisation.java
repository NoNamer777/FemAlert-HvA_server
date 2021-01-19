package nl.femalert.femserver.model.entity;

import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "[organisation]")
@NamedQueries({
    @NamedQuery(name = "find_all_organisation", query = "select O from Organisation O"),
    @NamedQuery(name = "find_organisation_by_id", query = "select O from Organisation O where O.id = ?1"),
    @NamedQuery(name = "find_organisation_by_name", query = "select O from Organisation O where O.name = ?1"),
})
public class Organisation implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_organisation")
    @GenericGenerator(
        name = "_seq_organisation",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "ORG-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d")
    })
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    protected Organisation() {}

    public Organisation(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
