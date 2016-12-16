/*
 Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
 © European Union, 2015-2016.

 This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
 redistribute it and/or modify it under the terms of the GNU General Public License as published by the
 Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
 the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
 copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */

package eu.europa.ec.fisheries.uvms.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Queue;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by osdjup on 2016-12-02.
 */
public class JMSUtils {

    private final static Logger LOG = LoggerFactory.getLogger(JMSUtils.class);

    public static Queue lookupQueue(InitialContext ctx, String queue) {
        try {
            return (Queue)ctx.lookup(queue);
        } catch (NamingException e) {
            //if we did not find the queue we might need to add java:/ at the start
            LOG.debug("Queue lookup failed for " + queue);
            String wfQueueName = "java:/" + queue;
            try {
                LOG.debug("trying " + wfQueueName);
                return (Queue)ctx.lookup(wfQueueName);
            } catch (Exception e2) {
                LOG.error("Queue lookup failed for both " + queue + " and " + wfQueueName);
                throw new RuntimeException(e);
            }
        }
    }

    public static Topic lookupTopic(InitialContext ctx, String topic) {
        try {
            return (Topic) ctx.lookup(topic);
        } catch (NamingException e) {
            //if we did not find the queue we might need to add java:/ at the start
            LOG.debug("Queue lookup failed for " + topic);
            String wfTopicName = "java:/" + topic;
            try {
                LOG.debug("trying " + wfTopicName);
                return (Topic)ctx.lookup(wfTopicName);
            } catch (Exception e2) {
                LOG.error("Topic lookup failed for both " + topic + " and " + wfTopicName);
                throw new RuntimeException(e);
            }
        }
    }
}
