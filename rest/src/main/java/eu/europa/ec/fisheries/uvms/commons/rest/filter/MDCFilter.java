package eu.europa.ec.fisheries.uvms.commons.rest.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * This filter ensures you that a request id is set on the MDC context for every incoming REST request.
 * If one was set already, that value will NOT be overridden.
 *
 * Use this filter by adding it to your web.xml config.
 * Example:
 * <filter>
 *     <filter-name>MDCFilter</filter-name>
 *     <filter-class>eu.europa.ec.fisheries.uvms.commons.rest.filter.MDCFilter</filter-class>
 * </filter>
 * <filter-mapping>
 *     <filter-name>MDCFilter</filter-name>
 *     <url-pattern>/rest/*</url-pattern>
 * </filter-mapping>
 *
 *
 * Make sure that your logger logs the MDC's requestId.
 * Example pattern for Logback: %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %X{requestId} %-5level %logger{40} %X{userId}- %msg%n
 *
 */
public class MDCFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(MDCFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.debug("MDC filter starting up");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (MDC.get("requestId") == null) {
            MDC.put("requestId", UUID.randomUUID().toString());
        }
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {
        LOG.debug("MDC filter shutting down");
    }

}