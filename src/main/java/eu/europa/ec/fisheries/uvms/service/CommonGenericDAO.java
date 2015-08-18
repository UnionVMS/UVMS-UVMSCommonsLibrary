package eu.europa.ec.fisheries.uvms.service;

import java.util.List;
import java.util.Map;

public interface CommonGenericDAO<T> {

    T createEntity(T entity) throws Exception;

    T updateEntity(T entity) throws Exception;

    T findEntityById(Class<T> entityClass, Object id) throws Exception;

    List<T> findEntityByNativeQuery(String hqlQuery) throws Exception;

    List<T> findEntityByNativeQuery(String hqlQuery, Map<String, String> parameters) throws Exception;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery) throws Exception;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters) throws Exception;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) throws Exception;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName) throws Exception;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters) throws Exception;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters, int maxResultLimit) throws Exception;

    List<T> findAllEntity(Class<T> entityClass) throws Exception;

    void deleteEntity(T entity, Object id) throws Exception;
}
