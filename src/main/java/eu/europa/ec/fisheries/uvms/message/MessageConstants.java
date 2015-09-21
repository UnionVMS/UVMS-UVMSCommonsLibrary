package eu.europa.ec.fisheries.uvms.message;

public class MessageConstants {

    private MessageConstants(){

    }

    public static final String CONNECTION_FACTORY = "java:/ConnectionFactory";
    public static final String CONNECTION_TYPE = "javax.jms.MessageListener";
    public static final String DESTINATION_TYPE_QUEUE = "javax.jms.Queue";

    public static final String QUEUE_MODULE_SPATIAL = "java:/jms/queue/UVMSSpatialEvent";
    public static final String QUEUE_MODULE_SPATIAL_NAME = "UVMSSpatialEvent";
    public static final String COMPONENT_MESSAGE_IN_QUEUE = "UVMSMovementEvent";
    public static final String COMPONENT_MESSAGE_IN_QUEUE_NAME = "UVMSMovementEvent";
    public static final String COMPONENT_RESPONSE_QUEUE = "java:/jms/queue/UVMSMovement";
    public static final String QUEUE_DATASOURCE_INTERNAL = "java:/jms/queue/UVMSMovementModel";
    public static final String QUEUE_VESSEL = "java:/jms/queue/UVMSVessel"; //response queue
    public static final String QUEUE_VESSEL_EVENT = "java:/jms/queue/UVMSVesselEvent"; //request queue, create event
    public static final String QUEUE_MODULE_MOVEMENT = "java:/jms/queue/UVMSMovementEvent";
    public static final String QUEUE_AUDIT_EVENT = "java:/jms/queue/UVMSAuditEvent";
    public static final String QUEUE_AUDIT = "java:/jms/queue/UVMSAudit";

}
