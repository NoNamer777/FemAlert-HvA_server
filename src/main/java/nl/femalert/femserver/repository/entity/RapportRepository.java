package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.Rapport;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("RAPPORTS.JPA")
public class RapportRepository extends AbstractEntityRepository<Rapport> {

    public RapportRepository() {
        super(Rapport.class);
    }
}
