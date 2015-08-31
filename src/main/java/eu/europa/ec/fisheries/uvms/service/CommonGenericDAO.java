package eu.europa.ec.fisheries.uvms.service;

import eu.europa.ec.fisheries.uvms.service.exception.CommonGenericDAOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public interface CommonGenericDAO<T> {

    T createEntity(T entity) throws CommonGenericDAOException;

    T updateEntity(T entity) throws CommonGenericDAOException;

    T findEntityById(Class<T> entityClass, Object id) throws CommonGenericDAOException;

    List<T> findEntityByNativeQuery(String hqlQuery) throws CommonGenericDAOException;

    List<T> findEntityByNativeQuery(String hqlQuery, Map<String, String> parameters) throws CommonGenericDAOException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery) throws CommonGenericDAOException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters) throws CommonGenericDAOException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) throws CommonGenericDAOException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName) throws CommonGenericDAOException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters) throws CommonGenericDAOException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters, int maxResultLimit) throws CommonGenericDAOException;

    List<T> findAllEntity(Class<T> entityClass) throws CommonGenericDAOException;

    void deleteEntity(T entity, Object id) throws CommonGenericDAOException;

    EntityManager getEntityManager();

}
