package nl.femalert.femserver.controller.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.entity.Address;
import nl.femalert.femserver.model.entity.Event;
import nl.femalert.femserver.model.entity.Rapport;
import nl.femalert.femserver.model.helper.exception.EntityIdConflictException;
import nl.femalert.femserver.model.helper.exception.EntityNotFoundException;
import nl.femalert.femserver.repository.generic.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static nl.femalert.femserver.controller.common.dataFetchers.*;

@RestController
@RequestMapping("/rapport")
public class RapportController {

    @Autowired
    private EntityRepository<Rapport> rapportRepo;

    @Autowired
    private EntityRepository<Event> eventRepo;

    @Autowired
    private EntityRepository<Address> addressRepo;

    @GetMapping
    public List<Rapport> getAllRapports() {
        return rapportRepo.findAll();
    }

    @GetMapping("/{id}")
    public Rapport getRapportById(@PathVariable String id) {
        Rapport foundRapport = rapportRepo.findById(id);

        if (foundRapport == null) throw new EntityNotFoundException(
            String.format("Rapport with ID: '%s' is not found.", id)
        );
        return foundRapport;
    }

    @PostMapping
    public ResponseEntity<Rapport> saveRapport(@RequestBody ObjectNode rapportData) {
        Rapport rapport = getRapportData(rapportData);
        rapport = rapportRepo.save(rapport);

        return ResponseEntity.ok(rapport);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rapport> updateRapport(@RequestBody ObjectNode rapportData, @PathVariable String id) {
        Rapport newRapportData = getRapportData(rapportData);
        Rapport oldRapport = getRapportById(id);

        if (!newRapportData.getId().equals(oldRapport.getId())) {
            throw new EntityIdConflictException(String.format(
                "Provided Rapport ID: '%s' is not the same as the ID of the Rapport ID in the request body: '%s'",
                newRapportData.getId(),
                oldRapport.getId()
            ));
        }
        newRapportData = rapportRepo.save(newRapportData);

        return ResponseEntity.ok(newRapportData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {
        Rapport foundRapport = getRapportById(id);

        rapportRepo.deleteById(foundRapport.getId());

        return ResponseEntity.ok().build();
    }

    private Rapport getRapportData(ObjectNode rapportData) {
        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String id = getStringValue(rapportData, "id");
        String name = getStringValue(rapportData, "name");
        String emailAddress = getStringValue(rapportData, "emailAddress");
        String story = getStringValue(rapportData, "story");
        String dateTime = getStringValue(rapportData, "dateTime");
        Boolean requiresSupport = getBooleanValue(rapportData, "requiresSupport");
        Boolean wantsExtraInfo = getBooleanValue(rapportData, "wantsExtraInfo");

        ArrayNode events = rapportData.get("events") == null ? null : rapportData.withArray("events");
        Set<Event> eventsData = getEvents(events);
        Address address = getAddress(rapportData);

        if (eventsData == null) throw new NullPointerException("No Events data is provided.");
        Rapport rapport = new Rapport(id);

        rapport.setName(name);
        rapport.setStory(story);
        rapport.setEmailAddress(emailAddress);
        rapport.setRequiresSupport(requiresSupport != null && requiresSupport);
        rapport.setWantsExtraInfo(wantsExtraInfo != null && wantsExtraInfo);
        rapport.setDateTime(dateTime == null ? null : LocalDateTime.from(DATE_TIME_FORMATTER.parse(dateTime)));
        rapport.setAddress(address);

        compareAndMergeEvents(eventsData, rapport);
        address.addRapport(rapport);

        return rapport;
    }

    private Set<Event> getEvents(ArrayNode data) {
        if (data == null) return null;
        Set<Event> events = new HashSet<>();

        for (JsonNode eventObj: data) {
            String eventId = getStringValue((ObjectNode) eventObj, "id");

            if (eventId == null) throw new NullPointerException("Could not find ID of Event object.");
            Event event = eventRepo.findById(eventId);

            if (event == null) throw new EntityNotFoundException(
                String.format("Event with ID: '%s' is not found.", eventId)
            );
            events.add(event);
        }
        return events;
    }

    private Address getAddress(ObjectNode rapportData) {
        String addressId = getObjectStringValue(rapportData, "address", "id");
        String formattedAddress = getObjectStringValue(rapportData, "address", "formattedAddress");
        String businessName = getObjectStringValue(rapportData, "address", "businessName");

        if (formattedAddress == null) throw new NullPointerException(
            "Address ID and / or address is missing from the provided data."
        );
        Address address;
        List<Address> foundAddresses = new ArrayList<>();
        if (addressId != null) {
            foundAddresses = addressRepo.findByQuery("find_address_by_id", addressId);
        }
        if (foundAddresses.isEmpty()) {
            foundAddresses = addressRepo.findByQuery("find_address_by_address", formattedAddress);
        }
        if (foundAddresses.isEmpty() && businessName != null) {
            foundAddresses = addressRepo.findByQuery("find_address_by_business", businessName);
        }
        if (foundAddresses.isEmpty()) {
            address = new Address(addressId);
            address.setFormattedAddress(formattedAddress);
            address.setBusinessName(businessName);

            address = addressRepo.save(address);
        } else {
            address = foundAddresses.get(0);
        }
        return address;
    }

    private void compareAndMergeEvents(Set<Event> events, Rapport rapport) {
        Set<Event> oldEvents = rapport.getEvents();

        // Remove old Events
        oldEvents.stream().filter(event -> events.isEmpty() || !events.contains(event)).forEach(event -> {
            rapport.removeEvent(event);
            event.removeRapport(rapport);
            eventRepo.save(event);
        });

        // Add new Events.
        events.stream().filter(event -> oldEvents.isEmpty() || !oldEvents.contains(event)).forEach(event -> {
            rapport.addEvent(event);
            event.addRapport(rapport);
            eventRepo.save(event);
        });
    }
}
