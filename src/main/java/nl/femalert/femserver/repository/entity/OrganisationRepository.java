package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.Organisation;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("ORGANISATIONS.JPA")
public class OrganisationRepository extends AbstractEntityRepository<Organisation> {

    public OrganisationRepository() {
        super(Organisation.class);
    }
}
