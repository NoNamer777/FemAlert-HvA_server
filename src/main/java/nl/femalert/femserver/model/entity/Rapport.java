package nl.femalert.femserver.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[rapport]")
@NamedQueries({
    @NamedQuery(name = "find_all_rapport", query = "select R from Rapport R"),
    @NamedQuery(name = "find_rapport_by_id", query = "select R from Rapport R where R.id = ?1"),
    @NamedQuery(name = "find_rapport_by_date", query = "select R from Rapport R where R.dateTime = ?1")
})
public class Rapport implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_rapport")
    @GenericGenerator(
        name = "_seq_rapport",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "RPT-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%08d")
    })
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "email_address", nullable = false)
    private String emailAddress;

    @JsonManagedReference
    @ManyToMany(targetEntity = Event.class)
    @JoinTable(
        name = "[rapport_event]",
        joinColumns = @JoinColumn(name = "rapport_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private final Set<Event> events = new HashSet<>();

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @JsonManagedReference
    @JoinColumn(name = "address_id", nullable = false)
    @ManyToOne(targetEntity = Address.class, optional = false)
    private Address address;

    @Column(name = "story")
    private String story;

    @Column(name = "requires_support", nullable = false)
    private Boolean requiresSupport;

    @Column(name = "extra_info_wanted", nullable = false)
    private Boolean wantsExtraInfo;

    protected Rapport() {}

    public Rapport(String id) {
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public boolean addEvent(Event event) {
        return events.add(event);
    }

    public boolean removeEvent(Event event) {
        return events.remove(event);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Boolean getRequiresSupport() {
        return requiresSupport;
    }

    public void setRequiresSupport(Boolean requiresSupport) {
        this.requiresSupport = requiresSupport;
    }

    public Boolean getWantsExtraInfo() {
        return wantsExtraInfo;
    }

    public void setWantsExtraInfo(Boolean wantsExtraInfo) {
        this.wantsExtraInfo = wantsExtraInfo;
    }
}
