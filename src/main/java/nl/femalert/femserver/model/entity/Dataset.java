package nl.femalert.femserver.model.entity;

import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@Entity
@Table(name = "[dataset]")
@NamedQueries({
    @NamedQuery(name = "find_all_dataset", query = "select D from Dataset D"),
    @NamedQuery(name = "find_dataset_by_id", query = "select D from Dataset D where D.id = ?1"),
    @NamedQuery(name = "find_dataset_by_name", query = "select D from Dataset D where D.name = ?1"),
})
public class Dataset implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_dataset")
    @GenericGenerator(
        name = "_seq_dataset",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "DTS-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%04d")
    })
    private String id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "basis", nullable = false)
    private String basis;

    @Column(name = "discriminator", nullable = false)
    private String discriminator;

    protected Dataset() {}

    public Dataset(String id) {
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

    public String getBasis() {
        return basis;
    }

    public void setBasis(String basis) {
        this.basis = basis;
    }

    public String getDiscriminator() {
        return discriminator;
    }

    public void setDiscriminator(String discriminator) {
        this.discriminator = discriminator;
    }
}
