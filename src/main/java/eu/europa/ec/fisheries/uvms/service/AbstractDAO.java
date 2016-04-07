package eu.europa.ec.fisheries.uvms.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public abstract class AbstractDAO<T extends Serializable> implements DAO<T> { //TODO rename to AbstractJpaDAO

    @Override
    public T createEntity(final T entity) throws ServiceException {
        getEntityManager().persist(entity);
        log.info("Persisting entity {}", entity);
        return entity;
    }

    @Override
    public T updateEntity(final T entity) throws ServiceException {
        log.info("Updating entity {}", entity);
        T merge = getEntityManager().merge(entity);
        log.info("to {}", merge);
        return merge;
    }

    @Override
    public T saveOrUpdateEntity(final T entity) throws ServiceException {
        log.info("Saving or Updating entity : " + entity.getClass().getSimpleName());
        return getEntityManager().merge(entity);
    }

    @Override
    public Integer updateEntityByNamedQuery(final String queryName) throws ServiceException {
        log.debug("Updating entity");
        Query namedQuery = getEntityManager().createNamedQuery(queryName);
        return namedQuery.executeUpdate();
    }

    @Override
    public T findEntityById(final Class<T> type, final Object id) throws ServiceException {
        log.info("Finding entity : " + type.getSimpleName() + " with ID : " + id.toString());
        return getEntityManager().find(type, id);
    }

    @Override
    public List<T> findEntityByNativeQuery(final Class<T> type, final String nativeQuery) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        return getEntityManager().createNativeQuery(nativeQuery, type).getResultList();
    }

    @Override
    public List<T> findEntityByNativeQuery(final Class<T> type, final String nativeQuery, final Map<String, String> parameters) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        Query query = getEntityManager().createNativeQuery(nativeQuery, type);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> type, final String hqlQuery) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        return getEntityManager().createQuery(hqlQuery, type).getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> type, final String hqlQuery, final Map<Integer, String> parameters) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        Set<Map.Entry<Integer, String>> rawParameters = parameters.entrySet();
        TypedQuery<T> query = getEntityManager().createQuery(hqlQuery, type);
        for (Map.Entry<Integer, String> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> type, final String hqlQuery, final Map<Integer, String> parameters, final int maxResultLimit) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        Set<Map.Entry<Integer, String>> rawParameters = parameters.entrySet();
        TypedQuery<T> query = getEntityManager().createQuery(hqlQuery, type);
        for (Map.Entry<Integer, String> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> type, final String queryName) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, type);
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> type, final String queryName, final Map<String, String> parameters) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, type);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> type, String queryName, final Map<String, String> parameters, final int maxResultLimit) throws ServiceException {
        log.info("Requesting {} with parameters {} and limit {}", queryName, parameters, maxResultLimit);
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, type);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findAllEntity(final Class<T> type) throws ServiceException {
        log.info("Finding all entity list for : " + type.getSimpleName());
        return getEntityManager().createQuery("FROM " + type.getSimpleName(), type).getResultList();
    }

    @Override
    public void deleteEntity(Class<T> type, final Object id) {
        log.info("Deleting entity : " + type.getSimpleName());
        Object ref = getEntityManager().getReference(type.getClass(), id);
        getEntityManager().remove(ref);
    }

    @Override
    public void deleteEntity(Object persistentObject) {
        log.info("Deleting entity : " + persistentObject.getClass().getSimpleName());
        getEntityManager().remove(persistentObject);
    }

    @Override
    public void deleteEntityByNamedQuery(final Class<T> type, final String queryName, final Map<String, String> parameters) throws ServiceException {
        log.info("Deleting entity : " + type.getSimpleName());
        Query query = getEntityManager().createNamedQuery(queryName);
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.executeUpdate();
    }

    public abstract EntityManager getEntityManager();


}
