package eu.europa.ec.fisheries.uvms.dao;

import org.hibernate.Query;

import java.util.Map;

public interface HibernateDAO<T> {

    Query createNamedNativeQuery(String nativeQueryString, Map<String, Object> parameters);
}
