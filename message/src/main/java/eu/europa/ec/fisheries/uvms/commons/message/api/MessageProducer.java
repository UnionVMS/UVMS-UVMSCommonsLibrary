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

package eu.europa.ec.fisheries.uvms.commons.message.api;

import java.util.Map;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Destination;
import javax.jms.TextMessage;

public interface MessageProducer {

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessageWithProps(String text, Destination replyTo, Map<String, String> props) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessageWithProps(String text, Destination replyTo, Map<String, String> props, int jmsDeliveryMode, long timeToLiveInMillis) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessage(String text, Destination replyTo) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendModuleMessageNonPersistent(String text, Destination replyTo, long timeToLiveInMillis) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendModuleResponseMessage(TextMessage message, String text, String moduleName);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendResponseMessageToSender(TextMessage message, String text) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendResponseMessageToSender(TextMessage message, String text, String moduleName) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendModuleResponseMessage(TextMessage message, String text) throws MessageException;

	String getDestinationName();

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    void sendFault(TextMessage textMessage, Fault fault);

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageWithSpecificIds(String messageToSend, Destination destination, Destination replyTo, String jmsMessageID, String jmsCorrelationID) throws MessageException;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    String sendMessageToSpecificQueue(String messageToSend, Destination destination, Destination replyTo) throws MessageException;
}
