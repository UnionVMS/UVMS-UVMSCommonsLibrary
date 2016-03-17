package eu.europa.ec.fisheries.uvms.interceptors;

import lombok.extern.slf4j.Slf4j;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

@Slf4j
public class TracingInterceptor {

    @AroundInvoke
    public Object logCall(InvocationContext context) throws Exception{

        Object[] parameters = context.getParameters();
        String params = "";
        for (Object parameter : parameters) {
            params += " " + parameter.toString();
        }

        log.info(
                String.format(
                        "User invoke method %s with parameters %s",
                        context.getMethod(),
                        params
                )
        );

        return context.proceed();
    }
}
