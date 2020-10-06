package nl.femalert.femserver.repository.entity;

import nl.femalert.femserver.model.entity.User;
import nl.femalert.femserver.repository.generic.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository("USERS.JPA")
public class UserRepository extends AbstractEntityRepository<User> {

    public UserRepository() {
        super(User.class);
    }
}
