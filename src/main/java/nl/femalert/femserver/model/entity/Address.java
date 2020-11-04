package nl.femalert.femserver.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import nl.femalert.femserver.model.helper.EntityIdGenerator;
import nl.femalert.femserver.model.helper.Identifiable;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "[address]")
@NamedQueries({
    @NamedQuery(name = "find_all_address", query = "select A from Address A"),
    @NamedQuery(name = "find_address_by_id", query = "select A from Address A where A.id = ?1"),
    @NamedQuery(name = "find_address_by_address", query = "select A from Address A where A.formattedAddress like ?1"),
    @NamedQuery(name = "find_address_by_business", query = "select A from Address A where A.businessName like ?1"),
})
public class Address implements Identifiable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "_seq_address")
    @GenericGenerator(
        name = "_seq_address",
        strategy = "nl.femalert.femserver.model.helper.EntityIdGenerator",
        parameters = {
            @Parameter(name = EntityIdGenerator.INCREMENT_PARAM, value = "1"),
            @Parameter(name = EntityIdGenerator.VALUE_PREFIX_PARAMETER, value = "ADR-"),
            @Parameter(name = EntityIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%07d")
    })
    private String id;

    @Column(name = "formatted_address", nullable = false)
    private String formattedAddress;

    @Column(name = "business_name")
    private String businessName;

    @JsonBackReference
    @OneToMany(targetEntity = Rapport.class, mappedBy = "address")
    private final Set<Rapport> rapports = new HashSet<>();

    protected Address() {}

    public Address(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Set<Rapport> getRapports() {
        return rapports;
    }

    public boolean addRapport(Rapport rapport) {
        return rapports.add(rapport);
    }

    public boolean removeRapport(Rapport rapport) {
        return rapports.remove(rapport);
    }
}
