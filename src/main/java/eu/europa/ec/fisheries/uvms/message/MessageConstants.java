/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.message;

public interface MessageConstants {

    String CONNECTION_FACTORY = "java:/ConnectionFactory";
    String CONNECTION_TYPE = "javax.jms.MessageListener";
    String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";

    String COMPONENT_MESSAGE_IN_QUEUE = "UVMSMovementEvent";
    String COMPONENT_MESSAGE_IN_QUEUE_NAME = "UVMSMovementEvent";
    String COMPONENT_RESPONSE_QUEUE = "java:/jms/queue/UVMSMovement";
    String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSMovementModel";
    String QUEUE_ASSET = "java:/jms/queue/UVMSAsset"; //response queue
    String QUEUE_ASSET_EVENT = "java:/jms/queue/UVMSAssetEvent"; //request queue, create event
    String QUEUE_MODULE_MOVEMENT = "java:/jms/queue/UVMSMovementEvent";
    String QUEUE_AUDIT_EVENT = "java:/jms/queue/UVMSAuditEvent";
    String QUEUE_AUDIT = "java:/jms/queue/UVMSAudit";
    String QUEUE_USM = "java:/jms/queue/UVMSUserEvent";
    String QUEUE_USM4UVMS = "java:/jms/queue/USM4UVMS";
    String QUEUE_CONFIG = "java:/jms/queue/UVMSConfigEvent";

    String QUEUE_MODULE_REPORTING_NAME = "UVMSReportingEvent";
    String QUEUE_REPORTING_EVENT = "java:/jms/queue/UVMSReportingEvent";
    String QUEUE_REPORTING = "java:/jms/queue/UVMSReporting";

    String QUEUE_MODULE_SPATIAL_NAME = "UVMSSpatialEvent";
    String QUEUE_MODULE_SPATIAL = "java:/jms/queue/UVMSSpatialEvent";
    String QUEUE_SPATIAL = "java:/jms/queue/UVMSSpatial";

    String QUEUE_MODULE_RULES = "java:/jms/queue/UVMSRulesEvent";

    String UPLOAD_QUEUE_NAME = "UVMSUploadEvent";
    String UPLOAD_EVENT_QUEUE = "java:/jms/queue/UVMSUploadEvent";
    String UPLOAD_QUEUE = "java:/jms/queue/UVMSUpload";


}
