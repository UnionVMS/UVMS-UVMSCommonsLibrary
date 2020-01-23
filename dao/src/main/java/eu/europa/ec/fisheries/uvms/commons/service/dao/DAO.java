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


import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;

import java.util.List;
import java.util.Map;

public interface DAO<T> {

    T createEntity(T entity) throws ServiceException;

    T createEntityAndFlush( T entity) throws ServiceException;

    T updateEntity(T entity) throws ServiceException;

    T saveOrUpdateEntity(T entity) throws ServiceException;

    T findEntityById(Class<T> type, Object id) throws ServiceException;

    List<T> findEntityByNativeQuery(Class<T> type, String hqlQuery) throws ServiceException;

    List<T> findEntityByNativeQuery(Class<T> type, String hqlQuery, Map<String, Object> parameters) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery, Map<Integer, Object> parameters) throws ServiceException;

    List<T> findEntityByHqlQuery(Class<T> type, String hqlQuery, Map<Integer, Object> parameters, int maxResultLimit) throws ServiceException;

    List<T> findEntityByNamedQuery(Class<T> type, String queryName) throws ServiceException;

    List<T> findEntityByNamedQuery(Class<T> type, String queryName, Map<String, Object> parameters) throws ServiceException;

    List<T> findEntityByNamedQuery(Class<T> type, String queryName, Map<String, Object> parameters, int maxResultLimit) throws ServiceException;

    List<T> findAllEntity(Class<T> type) throws ServiceException;

    void deleteEntity(Class<T> entity, Object id) throws ServiceException;

    void deleteEntityByNamedQuery(Class<T> type, String queryName, Map<String, Object> parameters) throws ServiceException;
}