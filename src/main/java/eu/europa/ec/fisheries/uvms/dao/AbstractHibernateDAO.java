package eu.europa.ec.fisheries.uvms.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;
import java.io.Serializable;
import java.util.Map;

@Slf4j
public abstract class AbstractHibernateDAO<T extends Serializable> implements HibernateDAO<T> {

    public Query createNamedNativeQuery(final String nativeQueryString, final Map<String, Object> parameters) {
        Query query = getSession().getNamedQuery(nativeQueryString);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query;
    }

    protected abstract Session getSession();
}
