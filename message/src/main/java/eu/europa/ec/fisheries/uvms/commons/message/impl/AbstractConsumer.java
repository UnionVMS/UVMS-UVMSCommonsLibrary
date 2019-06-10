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

package eu.europa.ec.fisheries.uvms.commons.message.impl;

import eu.europa.ec.fisheries.uvms.commons.message.context.MappedDiagnosticContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.jms.*;

public abstract class AbstractConsumer {

    private static long DEFAULT_TIME_TO_CONSUME = 120000;

    @Inject
    JMSContext context;

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumer.class);
    public abstract Destination getDestination();

    public <T> T getMessage(final String correlationId, Class<T> targetclazz ) throws JMSException {
        T  message = getMessage(correlationId,  targetclazz,DEFAULT_TIME_TO_CONSUME);
        if (message != null) {
            return message;
        }
        throw new RuntimeException("Message not received");
    }


    public <T> T getMessage(final String correlationId, Class<T> targetclazz,  Long timeoutInMillis) throws JMSException {
        try
        {
            if (correlationId == null || correlationId.isEmpty()) {
                throw new RuntimeException("No CorrelationID provided!");
            }
            Message  receivedMessage = context.createConsumer(getDestination()).receive( timeoutInMillis);
            if(receivedMessage != null) {
                MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext((Message) receivedMessage);
                return (T)  receivedMessage;
            }
            else{
                throw new JMSException("No Message retrieved");
            }
        } catch (final Exception e) {
            LOGGER.error("[ Error when retrieving message. ] {}", e.getMessage());
            throw new JMSException("No TextMessage retrieved");
        }
    }






}

