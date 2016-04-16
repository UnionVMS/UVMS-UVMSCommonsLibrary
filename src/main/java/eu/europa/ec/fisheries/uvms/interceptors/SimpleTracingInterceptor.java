package eu.europa.ec.fisheries.uvms.interceptors;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Slf4j
public class SimpleTracingInterceptor {

    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception {

        final Stopwatch stopwatch = Stopwatch.createStarted();

        try {
            log.info(String.format("invocation of method %s ", context.getMethod()));

            return context.proceed();
        }
        finally{
            log.info(String.format("Elapsed time ==> " + stopwatch));
        }
    }
}
