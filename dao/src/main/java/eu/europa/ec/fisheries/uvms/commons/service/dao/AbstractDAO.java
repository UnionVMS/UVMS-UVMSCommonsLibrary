/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.commons.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractDAO<T extends Serializable> implements DAO<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractDAO.class);

    @Override
    public T createEntity(final T entity) {
        LOG.debug("Persisting {}", entity.getClass().getSimpleName());
        getEntityManager().persist(entity);
        return entity;
    }

    @Override
    public T updateEntity(final T entity) {
        LOG.debug("Updating entity {}", entity.getClass().getSimpleName());
        T merge = getEntityManager().merge(entity);
        getEntityManager().flush();
        getEntityManager().refresh(merge);
        LOG.info("to {}", merge);
        return merge;
    }

    @Override
    public T saveOrUpdateEntity(final T entity) {
        LOG.debug("Saving or Updating entity : " + entity.getClass().getSimpleName());
        return getEntityManager().merge(entity);
    }

    @Override
    public T findEntityById(final Class<T> type, final Object id) {
        LOG.debug("Finding entity : " + type.getSimpleName() + " with ID : " + id.toString());
        return getEntityManager().find(type, id);
    }

    @Override
    public List<T> findEntityByNativeQuery(final Class<T> type, final String nativeQuery) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        return getEntityManager().createNativeQuery(nativeQuery, type).getResultList();
    }

    @Override
    public List<T> findEntityByNativeQuery(final Class<T> type, final String nativeQuery, final Map<String, Object> parameters) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        Query query = getEntityManager().createNativeQuery(nativeQuery, type);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> type, final String hqlQuery) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        return getEntityManager().createQuery(hqlQuery, type).getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> type, final String hqlQuery, final Map<Integer, Object> parameters) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        Set<Map.Entry<Integer, Object>> rawParameters = parameters.entrySet();
        TypedQuery<T> query = getEntityManager().createQuery(hqlQuery, type);
        for (Map.Entry<Integer, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByHqlQuery(final Class<T> type, final String hqlQuery, final Map<Integer, Object> parameters, final int maxResultLimit) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        Set<Map.Entry<Integer, Object>> rawParameters = parameters.entrySet();
        TypedQuery<T> query = getEntityManager().createQuery(hqlQuery, type);
        for (Map.Entry<Integer, Object> entry : rawParameters) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> type, final String queryName) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, type);
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> type, final String queryName, final Map<String, Object> parameters) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, type);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }

    @Override
    public List<T> findEntityByNamedQuery(final Class<T> type, String queryName, final Map<String, Object> parameters, final int maxResultLimit) {
        LOG.debug("Requesting {} with parameters {} and limit {}", queryName, parameters, maxResultLimit);
        TypedQuery<T> query = getEntityManager().createNamedQuery(queryName, type);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        if (maxResultLimit > 0) {
            query.setMaxResults(maxResultLimit);
        }
        return query.getResultList();
    }

    @Override
    public List<T> findAllEntity(final Class<T> type) {
        LOG.debug("Finding all entity list for : " + type.getSimpleName());
        return getEntityManager().createQuery("FROM " + type.getSimpleName(), type).getResultList();
    }

    @Override
    public void deleteEntity(Class<T> type, final Object id) {
        LOG.debug("Deleting entity : " + type.getSimpleName());
        Object ref = getEntityManager().getReference(type, id);
        getEntityManager().remove(ref);
    }

    @Override
    public void deleteEntityByNamedQuery(final Class<T> type, final String queryName, final Map<String, Object> parameters) {
        LOG.debug("Deleting entity : " + type.getSimpleName());
        Query query = getEntityManager().createNamedQuery(queryName);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        query.executeUpdate();
    }

    public abstract EntityManager getEntityManager();

}