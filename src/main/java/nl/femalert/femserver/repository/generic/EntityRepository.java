package nl.femalert.femserver.repository.generic;

import nl.femalert.femserver.model.helper.Identifiable;

import java.util.List;

public interface EntityRepository<E extends Identifiable> {

    List<E> findAll();

    List<E> findByQuery(String queryName, Object... params);

    E findById(String id);

    E save(E entity);

    void deleteById(String id);
}
