package eu.europa.ec.fisheries.uvms.interceptors;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Slf4j
public class TracingInterceptor {

    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception {

        final Stopwatch stopwatch = Stopwatch.createStarted();

        try {

            Object[] parameters = context.getParameters();
            String params = "";
            for (Object parameter : parameters) {
               params += " " + String.valueOf(parameter);
            }


            log.info(String.format("invocation of method %s with parameters %s", context.getMethod(), params));

            return context.proceed();

        }
        finally{
            log.info(String.format("Elapsed time ==> " + stopwatch));
        }
    }
}
