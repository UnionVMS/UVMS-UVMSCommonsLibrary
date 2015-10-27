package eu.europa.ec.fisheries.uvms.message;

public interface MessageConstants {

    String CONNECTION_FACTORY = "java:/ConnectionFactory";
    String CONNECTION_TYPE = "javax.jms.MessageListener";
    String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";

    String QUEUE_MODULE_SPATIAL = "java:/jms/queue/UVMSSpatialEvent";
    String QUEUE_MODULE_SPATIAL_NAME = "UVMSSpatialEvent";
    String COMPONENT_MESSAGE_IN_QUEUE = "UVMSMovementEvent";
    String COMPONENT_MESSAGE_IN_QUEUE_NAME = "UVMSMovementEvent";
    String COMPONENT_RESPONSE_QUEUE = "java:/jms/queue/UVMSMovement";
    String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSMovementModel";
    String QUEUE_VESSEL = "java:/jms/queue/UVMSVessel"; //response queue
    String QUEUE_VESSEL_EVENT = "java:/jms/queue/UVMSVesselEvent"; //request queue, create event
    String QUEUE_MODULE_MOVEMENT = "java:/jms/queue/UVMSMovementEvent";
    String QUEUE_AUDIT_EVENT = "java:/jms/queue/UVMSAuditEvent";
    String QUEUE_AUDIT = "java:/jms/queue/UVMSAudit";
    String QUEUE_REPORTING = "java:/jms/queue/UVMSReporting";
    String QUEUE_USM = "java:/jms/queue/UVMSUserEvent";
    String QUEUE_USM4UVMS = "java:/jms/queue/USM4UVMS";

}
