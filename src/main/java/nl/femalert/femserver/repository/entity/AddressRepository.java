package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.Address;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("ADDRESSES.JPA")
public class AddressRepository extends AbstractEntityRepository<Address> {

    public AddressRepository() {
        super(Address.class);
    }
}
