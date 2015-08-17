package eu.europa.ec.fisheries.uvms.service;

import eu.europa.ec.fisheries.uvms.service.exception.DAOException;

import java.util.List;
import java.util.Map;

public interface CommonGenericDAO<T> {

    T createEntity(T entity) throws DAOException;

    T updateEntity(T entity) throws DAOException;

    T findEntityById(Class<T> entityClass, Object id) throws DAOException;

    List<T> findEntityByNativeQuery(String hqlQuery) throws DAOException;

    List<T> findEntityByNativeQuery(String hqlQuery, Map<String, String> parameters) throws DAOException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery) throws DAOException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters) throws DAOException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) throws DAOException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName) throws DAOException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters) throws DAOException;

    List<T> findEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters, int maxResultLimit) throws DAOException;

    List<T> findAllEntity(Class<T> entityClass) throws DAOException;

    void deleteEntity(T entity, Object id) throws DAOException;
}
