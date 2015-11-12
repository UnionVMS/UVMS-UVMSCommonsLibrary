package eu.europa.ec.fisheries.uvms.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface DAO<T> {

    T createEntity(T entity) throws ServiceException;

    T updateEntity(T entity) throws ServiceException;

    T findEntityById(Class<T> entityClass, Object id) throws ServiceException;

    List<T> findEntityByNativeQuery(String hqlQuery) throws ServiceException;

    List<T> findEntityByNativeQuery(String hqlQuery, Map<String, String> parameters) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> entityClass, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) throws ServiceException;

    List<T> findEntityByNamedQuery(String queryName) throws ServiceException;

    List<T> findEntityByNamedQuery(String queryName, Map<String, String> parameters) throws ServiceException;

    List<T> findEntityByNamedQuery(String queryName, Map<String, String> parameters, int maxResultLimit) throws ServiceException;

    List<T> findAllEntity(Class<T> entityClass) throws ServiceException;

    void deleteEntity(T entity, Object id) throws ServiceException;

    void deleteEntityByNamedQuery(Class<T> entityClass, String queryName, Map<String, String> parameters) throws ServiceException;
}
