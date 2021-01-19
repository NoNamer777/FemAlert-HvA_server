package nl.femalert.femserver.controller.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.entity.Organisation;
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
@RequestMapping("/organisation")
public class OrganisationController {

    @Autowired
    private EntityRepository<Organisation> organisationRepo;

    @GetMapping
    public List<Organisation> getAllOrganisations() {
        return organisationRepo.findAll();
    }

    @GetMapping("/{id}")
    public Organisation getOrganisationById(@PathVariable String id) {
        Organisation foundOrganisation = organisationRepo.findById(id);

        if (foundOrganisation == null) {
            throw new EntityNotFoundException(String.format("Organisation with ID: '%s' is not found.", id));
        }
        return foundOrganisation;
    }

    @PostMapping
    public ResponseEntity<Organisation> saveOrganisation(@RequestBody ObjectNode organisationData) {
        Organisation newOrganisation = getOrganisationData(organisationData);
        List<Organisation> organisations = organisationRepo.findByQuery(
            "find_organisation_by_name",
            newOrganisation.getName()
        );

        if (!organisations.isEmpty()) throw new EntityAlreadyExistsException(
            String.format("Organisation which is called '%s' already exists.", newOrganisation.getName())
        );
        newOrganisation = organisationRepo.save(newOrganisation);

        return ResponseEntity.ok(newOrganisation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organisation> updateOrganisation(
            @RequestBody ObjectNode organisationData,
            @PathVariable String id
    ) {
        Organisation newOrganisationData = getOrganisationData(organisationData);
        Organisation oldOrganisation = getOrganisationById(id);

        if (!newOrganisationData.getId().equals(oldOrganisation.getId())) {
            throw new EntityIdConflictException(String.format(
                "Provided Organisation ID: '%s' is not the same as the ID of the Organisation ID in the request body: '%s'",
                newOrganisationData.getId(),
                oldOrganisation.getId()
            ));
        }
        newOrganisationData = organisationRepo.save(newOrganisationData);

        return ResponseEntity.ok(newOrganisationData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {
        Organisation foundOrganisation = getOrganisationById(id);

        organisationRepo.deleteById(foundOrganisation.getId());

        return ResponseEntity.ok().build();
    }

    public static Organisation getOrganisationData(ObjectNode organisationData) {
        String id = getStringValue(organisationData, "id");
        String name = getStringValue(organisationData, "name");
        String description = getStringValue(organisationData, "description");

        Organisation organisation = new Organisation(id);

        organisation.setName(name);
        organisation.setDescription(description);

        return organisation;
    }
}
