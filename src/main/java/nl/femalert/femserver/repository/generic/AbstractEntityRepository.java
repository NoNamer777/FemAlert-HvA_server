package nl.femalert.femserver.repository.generic;

import nl.femalert.femserver.model.helper.Identifiable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
public class AbstractEntityRepository<E extends Identifiable> implements EntityRepository<E> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<E> entityClass;

    public AbstractEntityRepository(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<E> findAll() {
        TypedQuery<E> query =
            entityManager.createNamedQuery(String.format("find_all_%s", getEntityClassString()), entityClass);

        return query.getResultList();
    }

    @Override
    public List<E> findByQuery(String queryName, Object... params) {
        TypedQuery<E> query =
                entityManager.createNamedQuery(queryName, entityClass);

        for (int idx = 0; idx < params.length; idx++) query.setParameter(idx + 1, params[idx]);
        return query.getResultList();
    }

    @Override
    public E findById(String id) {
        TypedQuery<E> query =
            entityManager.createNamedQuery(String.format("find_%s_by_id", getEntityClassString()), entityClass);

        query.setParameter(1, id);

        return query.getSingleResult();
    }

    @Override
    public E save(E entity) {
        if (entity.getId() == null) entityManager.persist(entity);
        else entityManager.merge(entity);

        return entity;
    }

    @Override
    public boolean deleteById(String id) {
        E entity = findById(id);

        entityManager.remove(entity);

        return findById(id) == null;
    }

    private String getEntityClassString() {
        char [] data = entityClass.getSimpleName().toCharArray();
        StringBuilder result = new StringBuilder();

        for (int idx = 0; idx < data.length; idx++) {
            if (Character.isUpperCase(data[idx]) && idx != 0) {
                result.append(String.format("_%s", Character.toLowerCase(data[idx])));
            } else {
                result.append(Character.toLowerCase(data[idx]));
            }
        }
        return result.toString();
    }
}
