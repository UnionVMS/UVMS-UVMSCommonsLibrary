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

import javax.annotation.Resource;
import javax.jms.*;

public abstract class AbstractConsumer  {

    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;


    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumer.class);


    public abstract Destination getDestination();

    public <T> T getMessage(final String correlationId, final Class type)  {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
             MessageConsumer consumer = session.createConsumer(getDestination(), "JMSCorrelationID='" + correlationId + "'");
        ) {
            if (correlationId == null || correlationId.isEmpty()) {
                throw new RuntimeException("No CorrelationID provided!");
            }
            final T receivedMessage = (T) consumer.receive();
            if (receivedMessage == null) {
                return null;
            }
            MappedDiagnosticContext.addMessagePropertiesToThreadMappedDiagnosticContext((TextMessage) receivedMessage);
            return receivedMessage;
        } catch (final Exception e) {
            LOGGER.error("[ Error when retrieving message. ] {}", e.getMessage());
            return null;
        }
    }




}
