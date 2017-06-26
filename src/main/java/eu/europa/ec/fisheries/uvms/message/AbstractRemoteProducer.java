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
package eu.europa.ec.fisheries.uvms.message;

import eu.europa.ec.fisheries.uvms.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.UUID;
/**
 * Created by sanera on 13/04/2017.
 */
public abstract class AbstractRemoteProducer implements MessageProducer {
    protected final static Logger LOG = LoggerFactory.getLogger(AbstractRemoteProducer.class);
    protected static final String FLUX_ENV_AD_VAL  = "XEU";
    protected static final String FLUX_ENV_DF_VAL  = "urn:un:unece:uncefact:fisheries:FLUX:FA:EU:2";
    protected static final String FLUX_ENV_AR_VAL = "true";
    protected static final String INITIAL_CONTEXT_FACTORY   = "org.jboss.naming.remote.client.InitialContextFactory";
    public static final String REMOTE_CONNECTION_FACTORY = "java:/jms/RemoteConnectionFactory";
    public static final String JMS_QUEUE_BRIDGE = "java:/jms/queue/bridge";
    public static final String CONNECTOR_ID     = "CONNECTOR_ID";
    public static final String FLUX_ENV_AD      = "AD";
    public static final String FLUX_ENV_DF      = "DF";
    public static final String BUSINESS_UUID    = "BUSINESS_UUID";
    public static final String FLUX_ENV_TODT    = "TODT";
    public static final String FLUX_ENV_AR = "AR";
    public static final String FLUX_ENV_FR = "FR";
    public static final String CONNECTOR_ID_VAL = "JMS MDM Business AP1";
    public static final String FLUX_ENV_TO = "TO";
    public static final String FLUX_ENV_CT = "CT";
    public static final String FLUX_ENV_VB = "VB";
    public static final String FLUX_ENV_TO_VAL = "60";
    public static final String FLUX_ENV_CT_VAL = "admin@dgmare.com";
    public static final String FLUX_ENV_VB_VAL = "ERROR";



    private TextMessageProperties textMessageProperties;
    private ConnectionProperties connectionProperties;
    private Queue bridgeQueue;
    private Connection connection;
    private Session session = null;

    private ConnectionFactory connectionFactory = null;

    @PostConstruct
    private void initializeProperties(){
        LOG.debug("initialize Properties for Abstract Remote Producer");
        textMessageProperties = new TextMessageProperties(FLUX_ENV_AD_VAL,FLUX_ENV_DF_VAL,FLUX_ENV_AR_VAL,FLUX_ENV_FR,createBusinessUUID(),createStringDate());
    }



    @Override
    public String sendModuleMessage(String text, Destination replyTo) throws MessageException {
        try {
            connectionProperties=getConnectionProperties();
            textMessageProperties = getTextMessageProperties();
            if(connectionProperties ==null)
                throw new ServiceException("JMS Connection properties are not initialized");

            openRemoteConnection();
            LOG.info(" Got connection to the Queue.");
            TextMessage MsgToSend = prepareMessage(text, session);
            LOG.debug("Text Message prepared with text :"+text);
            getProducer(bridgeQueue).send(MsgToSend);
            LOG.debug(">>> Message sent correctly to FLUX node. ID : [[ " + MsgToSend.getJMSMessageID() + " ]]");
            return MsgToSend.getJMSMessageID();
        } catch (Exception ex) {
            LOG.error("Error while trying to send message to FLUX node.", ex);
        }finally {
            closeConnection();
        }
        return null;
    }


    /**
     * Creates a MessageProducer for the given destination;
     *
     * @param destination
     * @return MessageProducer
     * @throws JMSException
     */
    private javax.jms.MessageProducer getProducer(Destination destination) throws JMSException {
        javax.jms.MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        producer.setTimeToLive(60000L);
        return producer;
    }

    /**
     * Prepare the message for sending and set minimal set of attributes, required by FLUX TL JMS
     * to understand how it should process it;
     *
     * @param textMessage
     * @return fluxMsg
     * @throws JMSException
     * @throws DatatypeConfigurationException
     */
    protected TextMessage prepareMessage(String textMessage, Session session) throws JMSException, ServiceException {
        if(textMessageProperties ==null)
            throw new ServiceException(" JMS TextMessage properties are not initialized");

        LOG.debug("Properties set on JMS message:" + textMessageProperties);
        TextMessage fluxMsg = session.createTextMessage();
        fluxMsg.setText(textMessage);
        fluxMsg.setStringProperty(CONNECTOR_ID, CONNECTOR_ID_VAL);
        fluxMsg.setStringProperty(FLUX_ENV_AD, textMessageProperties.getAdVal());
        fluxMsg.setStringProperty(FLUX_ENV_DF, textMessageProperties.getDfVal());
        fluxMsg.setStringProperty(BUSINESS_UUID, textMessageProperties.getBusinessUUId());
        fluxMsg.setStringProperty(FLUX_ENV_TODT, textMessageProperties.getCreationDate());
        fluxMsg.setStringProperty(FLUX_ENV_AR, textMessageProperties.getArVal());
        fluxMsg.setStringProperty(FLUX_ENV_FR, textMessageProperties.getFrVal());
        fluxMsg.setStringProperty(FLUX_ENV_TO, textMessageProperties.getToVal());
        fluxMsg.setStringProperty(FLUX_ENV_CT, textMessageProperties.getCtVal());
        fluxMsg.setStringProperty(FLUX_ENV_VB, textMessageProperties.getVbVal());
        printMessageProperties(fluxMsg);
        return fluxMsg;
    }

    private void printMessageProperties(TextMessage fluxMsg) throws JMSException {
        LOG.info("Prepared message with the following properties  : \n\n");
        int i = 0;
        Enumeration propertyNames = fluxMsg.getPropertyNames();
        String propName;
        while (propertyNames.hasMoreElements()) {
            i++;
            propName = (String) propertyNames.nextElement();
            LOG.debug(i + ". " + propName + " : " + fluxMsg.getStringProperty(propName));
        }
    }


    /**
     * Closes a JMS connection;
     * Disconnects from the actual connection if it is still active;
     */
    protected void closeConnection() {
        try {
            if (session != null) {
                LOG.debug("\n\nClosing session.");
                session.close();
            }
            if (connection != null) {
                LOG.debug("Succesfully closed the connection and/or session.");
                connection.stop();
                connection.close();
            }
            LOG.info("Succesfully disconnected from FLUX BRIDGE Remote queue.");
        } catch (JMSException e) {
            LOG.error("[ Error when stopping or closing JMS queue ] {}", e);
        }
    }

    private void openRemoteConnection() throws NamingException, JMSException {

        Context context = getContext();
        LOG.debug("Initial Context created");
        connectionFactory = (ConnectionFactory) context.lookup(REMOTE_CONNECTION_FACTORY);
        LOG.debug("Connection Factory received");
        bridgeQueue = (Queue) context.lookup(getDestinationName());
        LOG.debug("Bridge queue "+JMS_QUEUE_BRIDGE +" found.");

        try {
            LOG.debug("Opening connection to JMS broker:"+connectionProperties);
            connection = connectionFactory.createConnection(connectionProperties.getUsername(), connectionProperties.getPassword());
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            LOG.debug("session created");
        } catch (JMSException e) {
            LOG.error("Error when open connection to JMS broker. Going to << RETRY >> now.", e);
            retryConnecting();
        }

    }

    /**
     * Retry connecting to FLUX TL.
     *
     * @throws JMSException
     */
    private void retryConnecting() throws JMSException {
        LOG.debug("ReTrying to open connection.");
        try {
            connection = connectionFactory.createConnection(connectionProperties.getProviderURL(), connectionProperties.getPassword());
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException ex) {
            LOG.error("Error when retrying to open connection to Flux TL. Going to << FAIL >> now.", ex);
            throw ex;
        }
    }

    private Context getContext() throws NamingException {

        LOG.debug("Get initial Context.");
        Properties contextProps = new Properties();
        contextProps.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
        contextProps.put(Context.PROVIDER_URL, connectionProperties.getProviderURL());
        contextProps.put(Context.SECURITY_PRINCIPAL, connectionProperties.getUsername());
        contextProps.put(Context.SECURITY_CREDENTIALS, connectionProperties.getPassword());
        return new InitialContext(contextProps);

    }

    public TextMessageProperties getTextMessageProperties() {
        return textMessageProperties;
    }

    public void setTextMessageProperties(TextMessageProperties textMessageProperties) {
        this.textMessageProperties = textMessageProperties;
    }

    public abstract ConnectionProperties getConnectionProperties() ;




    /**
     * BUSINESS_UUID has a prefix, a date-time combination and a serial - thus it is semi unique
     *
     * @return randomUUID
     */
    protected String createBusinessUUID() {
        return UUID.randomUUID().toString();
    }

    private String createStringDate() {
        GregorianCalendar gcal = (GregorianCalendar) Calendar.getInstance();
        gcal.setTime(new Date(System.currentTimeMillis() + 1000000));
        XMLGregorianCalendar xgcal = null;
        try {
            xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
            return xgcal.toString();
        } catch (DatatypeConfigurationException | NullPointerException e) {
            LOG.error("Error occured while creating newXMLGregorianCalendar", e);
            return null;
        }
    }
}
