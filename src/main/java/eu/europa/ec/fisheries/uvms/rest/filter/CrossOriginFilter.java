package eu.europa.ec.fisheries.uvms.rest.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import eu.europa.ec.fisheries.uvms.CommonConstants;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * @author jojoha
 */
@WebFilter(asyncSupported = true, urlPatterns = {"/*"})
public class CrossOriginFilter implements Filter {

    final static Logger LOG = LoggerFactory.getLogger(CrossOriginFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("Requstfilter starting up!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader(CommonConstants.ACCESS_CONTROL_ALLOW_ORIGIN, CommonConstants.ACCESS_CONTROL_ALLOW_METHODS_ALL);
        response.setHeader(CommonConstants.ACCESS_CONTROL_ALLOW_METHODS, CommonConstants.ACCESS_CONTROL_ALLOWED_METHODS);
        response.setHeader(CommonConstants.ACCESS_CONTROL_ALLOW_HEADERS, CommonConstants.ACCESS_CONTROL_ALLOW_HEADERS_ALL);
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {
        LOG.info("Requstfilter shuting down!");
    }

}
