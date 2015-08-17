package eu.europa.ec.fisheries.uvms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * //TODO create test
 */
/**
 * This class is responsible for all application level database interaction.
 * It provides unified apis for all basic CRUD operations like Create, Read, Update, Delete.
 */
public abstract class JPACommonGenericDAO<T> implements CommonGenericDAO<T> {

    private static final Logger LOG = LoggerFactory.getLogger(JPACommonGenericDAO.class);

    @Override
    public T createEntity(final T entity) {
        LOG.debug("Persisting entity : " + entity.getClass().getSimpleName());
        getEntityManager().persist(entity);
        return entity;
    }

    @Override
    public T updateEntity(final T entity) {
        LOG.debug("Updating entity : " + entity.getClass().getSimpleName());
        getEntityManager().merge(entity);
        return entity;
    }

    @Override
    public T findEntityById(final Class<T> entityClass, final Object id) {
        LOG.debug("Finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public List<T> findEntityByNativeQuery(String nativeQuery) {
        LOG.debug("Finding entity for query : " + nativeQuery);
        return getEntityManager().createNativeQuery(nativeQuery).getResultList();
    }

    @Override
    public List<T> findEntityByNativeQuery(String nativeQuery, Map<String, String> parameters) {
        LOG.debug("Finding entity for query : " + nativeQuery);
        Query query = getEntityManager().createNativeQuery(nativeQuery);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> entityClass, final String hqlQuery) {
        LOG.debug("Finding entity for query : " + hqlQuery);
        return getEntityManager().createQuery(hqlQuery, entityClass).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByHqlQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters) {
        LOG.debug("Finding entity for query : " + hqlQuery);
        Set<Map.Entry<Integer, String>> rawParameters = parameters.entrySet();
        Query query = getEntityManager().createQuery(hqlQuery, entityClass);
        for (Map.Entry<Integer, String> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByHqlQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters, final int maxResultLimit) {
        LOG.debug("Finding entity for query : " + hqlQuery);
        Set<Map.Entry<Integer, String>> rawParameters = parameters.entrySet();
        Query query = getEntityManager().createQuery(hqlQuery, entityClass);
        for (Map.Entry<Integer, String> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> entityClass, final String queryName) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, entityClass);
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, entityClass);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters, int maxResultLimit) {
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, entityClass);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findAllEntity(final Class<T> entityClass) {
        LOG.debug("Finding all entity list for : " + entityClass.getSimpleName());
        return getEntityManager().createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    @Override
    public void deleteEntity(final T entity, final Object id) {
        LOG.debug("Deleting entity : " + entity.getClass().getSimpleName());
        getEntityManager().remove(getEntityManager().contains(entity) ? entity : getEntityManager().merge(entity));
    }

    protected abstract EntityManager getEntityManager();

}
