package eu.europa.ec.fisheries.uvms.service.interceptor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.Nonbinding;
import javax.interceptor.InterceptorBinding;

import eu.europa.ec.fisheries.uvms.common.AuditActionEnum;

@Inherited
@InterceptorBinding
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IAuditInterceptor {
	
	@Nonbinding AuditActionEnum auditActionType() default AuditActionEnum.UNDEFINED;

}
