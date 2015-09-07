package eu.europa.ec.fisheries.uvms.rest.security;

/**
 * Defines all known features
 */
public enum UnionVMSFeature {
    /** View Vessels */
    getVesselList,
    getVesselById,
    /** Create vessels */
    createVessel,
    /** Update vessels */
    updateVessel,
    /** Access vessel history */
    vesselHistory,
    /** View vessel groups */
    viewVesselGroups,
    /** Manage vessel groups */
    manageVesselGroups,
    /** Accessing vessel configuration */
    vesselConfig,

    /** Viewing mobile terminals */
    viewMobileTerminals,
    /** Managing mobile terminals */
    manageMobileTerminals,
    /** Mobile terminal plugins */
    mobileTerminalPlugins,

    /** Viewing positions */
    viewPositions,
    /** Managing positions */
    managePositions,

    /** Viewing exchange logs */
    viewExchangeLog,
    /** Resending exchange logs */
    resendExchangeLog,
    /** Forwarding exchange logs */
    forwardExchangeLog,
    /** Viewing sending queue */
    viewSendingQueue,
    /** Pausing messages in sending queue */
    manageSendingQueue,
    /** Viewing transmission status */
    viewTransmissionStatus,
    /** Starting and stopping transmissions */
    manageTransmissionStatus,

    /** Viewing polling logs */
    viewPollingLogs,
    /** Managing program polls */
    manageProgramPolls,
    /** Creating new polls */
    managePolls,

    /** Viewing audit logs */
    viewAuditLogs
}
