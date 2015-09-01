package eu.europa.ec.fisheries.uvms.service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public interface CrudService<T> {

    T createEntity(T entity);

    T updateEntity(T entity);

    T findEntityById(Class<T> entityClass, Object id);

    List<T> findEntityByNativeQuery(String hqlQuery);

    List<T> findEntityByNativeQuery(String hqlQuery, Map<String, String> parameters);

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery);

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters);

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit);

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName);

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters);

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters, int maxResultLimit);

    List<T> findAllEntity(Class<T> entityClass);

    void deleteEntity(T entity, Object id);

    EntityManager getEntityManager();

}
