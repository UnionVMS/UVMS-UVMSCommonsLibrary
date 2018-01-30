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

public interface MessageConstants {

    String CONNECTION_FACTORY = "ConnectionFactory";
    String CONNECTION_TYPE = "javax.jms.MessageListener";
    String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";
    String DESTINATION_TYPE_TOPIC = "javax.jms.Topic";

    String SUBSCRIPTION_DURABILITY_STR = "subscriptionDurability";
    String DURABLE_CONNECTION = "Durable";
    String SUBSCRIPTION_NAME_STR = "subscriptionName";
    String CLIENT_ID_STR = "clientId";
    String MESSAGE_SELECTOR_STR = "messageSelector";

    String MESSAGING_TYPE_STR = "messagingType";
    String DESTINATION_TYPE_STR = "destinationType";
    String DESTINATION_STR = "destination";
    String DESTINATION_JNDI_NAME = "destinationJndiName";
    String DESTINATION_FACTORY_JNDI_NAME = "connectionFactoryJndiName";

    String QUEUE_SUBSCRIPTION_EVENT = "jms/queue/UVMSSubscriptionEvent";
    String QUEUE_NAME_SUBSCRIPTION_EVENT = "UVMSSubscriptionEvent";

    String COMPONENT_MESSAGE_IN_QUEUE = "UVMSMovementEvent";
    String COMPONENT_MESSAGE_IN_QUEUE_NAME = "UVMSMovementEvent";
    String COMPONENT_RESPONSE_QUEUE = "jms/queue/UVMSMovement";
    String QUEUE_DATASOURCE_INTERNAL = "jms/queue/UVMSMovementModel";

    String QUEUE_ASSET = "jms/queue/UVMSAsset"; // response queue
    String QUEUE_ASSET_EVENT = "jms/queue/UVMSAssetEvent"; // request queue, create event

    String QUEUE_MODULE_MOVEMENT = "jms/queue/UVMSMovementEvent";
    String QUEUE_AUDIT_EVENT = "jms/queue/UVMSAuditEvent";
    String QUEUE_AUDIT = "jms/queue/UVMSAudit";

    String QUEUE_USM = "jms/queue/UVMSUserEvent";
    String QUEUE_USM4UVMS = "jms/queue/USM4UVMS";
    String QUEUE_CONFIG = "jms/queue/UVMSConfigEvent";

    String QUEUE_MODULE_REPORTING_NAME = "UVMSReportingEvent";
    String QUEUE_REPORTING_EVENT = "jms/queue/UVMSReportingEvent";
    String QUEUE_REPORTING = "jms/queue/UVMSReporting";

    String QUEUE_MODULE_SPATIAL_NAME = "UVMSSpatialEvent";
    String QUEUE_MODULE_SPATIAL = "jms/queue/UVMSSpatialEvent";
    String QUEUE_SPATIAL = "jms/queue/UVMSSpatial";

    String QUEUE_MODULE_RULES = "jms/queue/UVMSRulesEvent";
    String QUEUE_RULES = "jms/queue/UVMSRules";
    String RULES_MESSAGE_IN_QUEUE_NAME = "UVMSRulesEvent";

    String UPLOAD_QUEUE_NAME = "UVMSUploadEvent";
    String UPLOAD_EVENT_QUEUE = "jms/queue/UVMSUploadEvent";
    String UPLOAD_QUEUE = "jms/queue/UVMSUpload";

    String QUEUE_MODULE_ACTIVITY_NAME = "UVMSActivityEvent";
    String QUEUE_MODULE_ACTIVITY = "jms/queue/UVMSActivityEvent";
    String QUEUE_ACTIVITY = "jms/queue/UVMSActivity";

    String QUEUE_MDR = "jms/queue/UVMSMdr";
    String QUEUE_MDR_EVENT = "jms/queue/UVMSMdrEvent";
    String MDR_MESSAGE_IN_QUEUE_NAME = "UVMSMdrEvent";

    String QUEUE_EXCHANGE = "jms/queue/UVMSExchange";
    String QUEUE_EXCHANGE_EVENT = "jms/queue/UVMSExchangeEvent";

    String QUEUE_SALES = "jms/queue/UVMSSales";
    String QUEUE_SALES_EVENT = "jms/queue/UVMSSalesEvent";
    String QUEUE_ECB_PROXY = "jms/queue/UVMSSalesEcbProxy";

    String FLUX_MDR_REMOTE_MESSAGE_IN_QUEUE_NAME = "UVMSMdrPluginEvent";
    String FLUX_MDR_REMOTE_MESSAGE_OUT_QUEUE_NAME = "UVMSMdrPlugin";

    String EVENTBUS_TOPIC = "jms/topic/EventBus";
    String EVENTBUS_TOPIC_NAME = "EventBus";

    String QUEUE_PLUGIN_BRIDGE = "jms/queue/UVMSPluginBridge";
    String QUEUE_MOBILE_TERMINAL_EVENT = "jms/queue/UVMSMobileTerminalEvent";
}
