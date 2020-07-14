/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2020.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/
package eu.europa.ec.fisheries.uvms.commons.message.context;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.jms.Message;

/**
 * Implement the propagation of the FLUX envelope data.
 *
 * For JMS {@code onMessage} methods, it extracts the data from the first argument (the {@code Message}).
 */
@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@PropagateFluxEnvelopeData
public class PropagateFluxEnvelopeDataInterceptor {

	@Inject
	private FluxEnvelopeStack fluxEnvelopeStack;

	@Inject
	private JmsFluxEnvelopeHelper fluxEnvelopeHelper;

	@AroundInvoke
	public Object intercept(InvocationContext ctx) throws Exception {
		Message message = ctx.getParameters().length > 0 && ctx.getParameters()[0] instanceof Message ? (Message) ctx.getParameters()[0] : null;
		try {
			FluxEnvelopePropagatedData data = fluxEnvelopeHelper.extract(message);
			fluxEnvelopeStack.push(data);
			return ctx.proceed();
		} finally {
			fluxEnvelopeStack.pop();
		}
	}
}
