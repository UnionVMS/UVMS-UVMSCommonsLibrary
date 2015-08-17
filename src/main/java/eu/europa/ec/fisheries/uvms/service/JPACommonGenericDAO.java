package eu.europa.ec.fisheries.uvms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
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

    @PersistenceContext(unitName = "UVMS")
    EntityManager em;

    @Override
    public T createEntity(final T entity) {
        LOG.debug("Persisting entity : " + entity.getClass().getSimpleName());
        em.persist(entity);
        return entity;
    }

    @Override
    public T updateEntity(final T entity) {
        LOG.debug("Updating entity : " + entity.getClass().getSimpleName());
        em.merge(entity);
        return entity;
    }

    @Override
    public T findEntityById(final Class<T> entityClass, final Object id) {
        LOG.debug("Finding entity : " + entityClass.getSimpleName() + " with ID : " + id.toString());
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findEntityByNativeQuery(String nativeQuery) {
        LOG.debug("Finding entity for query : " + nativeQuery);
        return em.createNativeQuery(nativeQuery).getResultList();
    }

    @Override
    public List<T> findEntityByNativeQuery(String nativeQuery, Map<String, String> parameters) {
        LOG.debug("Finding entity for query : " + nativeQuery);
        Query query = em.createNativeQuery(nativeQuery);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> entityClass, final String hqlQuery) {
        LOG.debug("Finding entity for query : " + hqlQuery);
        return em.createQuery(hqlQuery, entityClass).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntityByHqlQuery(final Class<T> entityClass, final String hqlQuery, final Map<Integer, String> parameters) {
        LOG.debug("Finding entity for query : " + hqlQuery);
        Set<Map.Entry<Integer, String>> rawParameters = parameters.entrySet();
        Query query = em.createQuery(hqlQuery, entityClass);
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
        Query query = em.createQuery(hqlQuery, entityClass);
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
        TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters) {
        TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters, int maxResultLimit) {
        TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
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
        return em.createQuery("from " + entityClass.getSimpleName(), entityClass).getResultList();
    }

    @Override
    public void deleteEntity(final T entity, final Object id) {
        LOG.debug("Deleting entity : " + entity.getClass().getSimpleName());
        em.remove(em.contains(entity) ? entity : em.merge(entity));
    }

}
