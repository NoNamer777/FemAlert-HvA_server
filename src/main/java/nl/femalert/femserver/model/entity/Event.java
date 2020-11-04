package nl.femalert.femserver.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "[event]")
@NamedQueries({
    @NamedQuery(name = "find_all_event", query = "select E from Event E"),
    @NamedQuery(name = "find_event_by_id", query = "select E from Event E where E.id = ?1"),
    @NamedQuery(name = "find_event_by_name", query = "select E from Event E where E.name = ?1"),
    @NamedQuery(name = "find_active_events", query = "select E from Event E where E.active = true"),
})
public class Event implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_event")
    @GenericGenerator(
        name = "_seq_event",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "EVT-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d")
    })
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "removable", nullable = false)
    private Boolean removable;

    @JsonBackReference
    @ManyToMany(targetEntity = Rapport.class, mappedBy = "events")
    private final Set<Rapport> rapports = new HashSet<>();

    protected Event() {};

    public Event(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;

        return getId().equals(event.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getRemovable() {
        return removable;
    }

    public void setRemovable(Boolean removable) {
        this.removable = removable;
    }

    public Set<Rapport> getRapports() {
        return rapports;
    }

    public boolean addRapport(Rapport rapport) {
        return rapports.add(rapport);
    }

    public boolean removeRapport(Rapport rapport) {
        return rapports.remove(rapport);
    }
}
