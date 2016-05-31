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
