package nl.femalert.femserver.controller.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.entity.Event;
import nl.femalert.femserver.model.helper.exception.EntityAlreadyExistsException;
import nl.femalert.femserver.model.helper.exception.EntityIdConflictException;
import nl.femalert.femserver.model.helper.exception.EntityNotFoundException;
import nl.femalert.femserver.repository.generic.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.femalert.femserver.controller.common.dataFetchers.getBooleanValue;
import static nl.femalert.femserver.controller.common.dataFetchers.getStringValue;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EntityRepository<Event> eventRepo;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable String id) {
        Event foundEvent = eventRepo.findById(id);

        if (foundEvent == null) {
            throw new EntityNotFoundException(String.format("Event with ID: '%s' is not found.", id));
        }
        return foundEvent;
    }

    @PostMapping
    public ResponseEntity<Event> saveEvent(@RequestBody ObjectNode eventData) {
        Event newEvent = getEventData(eventData);
        List<Event> events = eventRepo.findByQuery("find_event_by_name", newEvent.getName());

        if (!events.isEmpty()) throw new EntityAlreadyExistsException(
            String.format("Event which is called '%s' already exists.", newEvent.getName())
        );
        newEvent = eventRepo.save(newEvent);

        return ResponseEntity.ok(newEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@RequestBody ObjectNode eventData, @PathVariable String id) {
        Event newEventData = getEventData(eventData);
        Event oldEvent = getEventById(id);

        if (!newEventData.getId().equals(oldEvent.getId())) {
            throw new EntityIdConflictException(String.format(
                "Provided Event ID: '%s' is not the same as the ID of the Event ID in the request body: '%s'",
                newEventData.getId(),
                oldEvent.getId()
            ));
        }
        newEventData = eventRepo.save(newEventData);

        return ResponseEntity.ok(newEventData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {
        Event foundEvent = getEventById(id);

        eventRepo.deleteById(foundEvent.getId());

        return ResponseEntity.ok().build();
    }

    public static Event getEventData(ObjectNode eventData) {
        String id = getStringValue(eventData, "id");
        String name = getStringValue(eventData, "name");
        Boolean active = getBooleanValue(eventData, "active");
        Boolean removable = getBooleanValue(eventData, "removable");

        Event event = new Event(id);

        event.setName(name);
        event.setActive(active != null && active);
        event.setRemovable(removable != null && removable);

        return event;
    }
}
