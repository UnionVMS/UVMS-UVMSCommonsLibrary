/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

*/

package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JMSUtils {

    private final static Logger LOG = LoggerFactory.getLogger(JMSUtils.class);

    public static Queue lookupQueue(InitialContext ctx, String queue) {
        try {
            return (Queue)ctx.lookup(queue);
        } catch (NamingException e) {
            //if we did not find the queue we might need to add java:/ at the start
            LOG.debug("Queue lookup failed for " + queue);
            String wfQueueName = "java:/"+ queue;
            try {
                LOG.debug("trying " + wfQueueName);
                return (Queue)ctx.lookup(wfQueueName);
            } catch (Exception e2) {
                LOG.error("Queue lookup failed for both " + queue + " and " + wfQueueName);
                throw new RuntimeException(e);
            }
        }
    }
	
	
    public static ConnectionFactory lookupConnectionFactory() {
		ConnectionFactory connectionFactory = null;
        LOG.debug("Open connection to JMS broker");
        InitialContext ctx;
        try {
            ctx = new InitialContext();
        } catch (Exception e) {
            LOG.error("Failed to get InitialContext",e);
            throw new RuntimeException(e);
        }
        try {
            connectionFactory = (QueueConnectionFactory) ctx.lookup(MessageConstants.CONNECTION_FACTORY);
        } catch (NamingException ne) {
            //if we did not find the connection factory we might need to add java:/ at the start
            LOG.debug("Connection Factory lookup failed for " + MessageConstants.CONNECTION_FACTORY);
            String wfName = "java:/" + MessageConstants.CONNECTION_FACTORY;
            try {
                LOG.debug("trying " + wfName);
                connectionFactory = (QueueConnectionFactory) ctx.lookup(wfName);
            } catch (Exception e) {
                LOG.error("Connection Factory lookup failed for both " + MessageConstants.CONNECTION_FACTORY  + " and " + wfName);
                throw new RuntimeException(e);
            }
        }
		
		return connectionFactory;
    }	
	
}