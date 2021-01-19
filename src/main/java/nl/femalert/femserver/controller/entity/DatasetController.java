package nl.femalert.femserver.controller.entity;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.femalert.femserver.model.entity.Dataset;
import nl.femalert.femserver.model.helper.exception.EntityAlreadyExistsException;
import nl.femalert.femserver.model.helper.exception.EntityIdConflictException;
import nl.femalert.femserver.model.helper.exception.EntityNotFoundException;
import nl.femalert.femserver.repository.generic.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.femalert.femserver.controller.common.dataFetchers.getStringValue;

@RestController
@RequestMapping("/dataset")
public class DatasetController {

    @Autowired
    private EntityRepository<Dataset> datasetRepo;

    @GetMapping
    public List<Dataset> getAllDatasets() {
        return datasetRepo.findAll();
    }

    @GetMapping("/{id}")
    public Dataset getDatasetById(@PathVariable String id) {
        Dataset foundDataset = datasetRepo.findById(id);

        if (foundDataset == null) {
            throw new EntityNotFoundException(String.format("Dataset with ID: '%s' is not found.", id));
        }
        return foundDataset;
    }

    @PostMapping
    public ResponseEntity<Dataset> saveDataset(@RequestBody ObjectNode datasetData) {
        Dataset newDataset = getDatasetData(datasetData);
        List<Dataset> datasets = datasetRepo.findByQuery(
            "find_dataset_by_name",
            newDataset.getName()
        );

        if (!datasets.isEmpty()) throw new EntityAlreadyExistsException(
            String.format("Dataset which is called '%s' already exists.", newDataset.getName())
        );
        newDataset = datasetRepo.save(newDataset);

        return ResponseEntity.ok(newDataset);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dataset> updateDataset(
            @RequestBody ObjectNode datasetData,
            @PathVariable String id
    ) {
        Dataset newDatasetData = getDatasetData(datasetData);
        Dataset oldDataset = getDatasetById(id);

        if (!newDatasetData.getId().equals(oldDataset.getId())) {
            throw new EntityIdConflictException(String.format(
                "Provided Dataset ID: '%s' is not the same as the ID of the Dataset ID in the request body: '%s'",
                newDatasetData.getId(),
                oldDataset.getId()
            ));
        }
        newDatasetData = datasetRepo.save(newDatasetData);

        return ResponseEntity.ok(newDatasetData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable String id) {
        Dataset foundDataset = getDatasetById(id);

        datasetRepo.deleteById(foundDataset.getId());

        return ResponseEntity.ok().build();
    }

    private Dataset getDatasetData(ObjectNode datasetData) {
        String id = getStringValue(datasetData, "id");
        String name = getStringValue(datasetData, "name");
        String basis = getStringValue(datasetData, "basis");
        String discriminator = getStringValue(datasetData, "discriminator");

        Dataset dataset = new Dataset(id);

        dataset.setName(name);
        dataset.setBasis(basis);
        dataset.setDiscriminator(discriminator);

        return dataset;
    }
}
