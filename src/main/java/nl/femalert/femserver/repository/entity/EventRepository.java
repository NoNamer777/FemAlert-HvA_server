package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.Event;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("EVENTS.JPA")
public class EventRepository extends AbstractEntityRepository<Event> {

    public EventRepository() {
        super(Event.class);
    }
}
