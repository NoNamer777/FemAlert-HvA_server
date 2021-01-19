package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.Dataset;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("DATASETS.JPA")
public class DatasetRepository extends AbstractEntityRepository<Dataset> {

    public DatasetRepository() {
        super(Dataset.class);
    }
}
