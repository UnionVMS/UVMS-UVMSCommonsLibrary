package eu.europa.ec.fisheries.uvms.service;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface DAO<T> {

    T createEntity(T entity) throws ServiceException;

    T updateEntity(T entity) throws ServiceException;

    int updateEntityByNamedQuery(String queryName) throws ServiceException;

    T saveOrUpdateEntity(T entity) throws ServiceException;

    T findEntityById(Class<T> type, Object id) throws ServiceException;

    List<T> findEntityByNativeQuery(Class<T> type, String hqlQuery) throws ServiceException;

    List<T> findEntityByNativeQuery(Class<T> type, String hqlQuery, Map<String, String> parameters) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery, Map<Integer, String> parameters) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery, Map<Integer, String> parameters, int maxResultLimit) throws ServiceException;

    List<T> findEntityByNamedQuery(Class<T> type, String queryName) throws ServiceException;

    List<T> findEntityByNamedQuery(Class<T> type, String queryName, Map<String, String> parameters) throws ServiceException;

    List<T> findEntityByNamedQuery(Class<T> type, String queryName, Map<String, String> parameters, int maxResultLimit) throws ServiceException;

    List<T> findAllEntity(Class<T> type) throws ServiceException;

    void deleteEntity(Object persistentObject);

    void deleteEntity(Class<T> entity, Object id) throws ServiceException;

    void deleteEntityByNamedQuery(Class<T> type, String queryName, Map<String, String> parameters) throws ServiceException;
}
