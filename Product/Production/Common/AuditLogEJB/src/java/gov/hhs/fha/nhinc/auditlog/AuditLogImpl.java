package gov.hhs.fha.nhinc.auditlog;

import gov.hhs.fha.nhinc.auditrepositoryproxy.AuditRepositoryProxyFactory;
import gov.hhs.fha.nhinc.auditrepositoryproxy.IAuditRepositoryProxy;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogAdhocQueryResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogDocRetrieveResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogFindAuditEventsResultRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogNhinSubjectDiscoveryAckRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectAddedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectReidentificationResponseType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevisedRequestType;
import gov.hhs.fha.nhinc.common.auditlog.LogSubjectRevokedRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityCdcNotifyRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityCdcSubscribeRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.EntityUnsubscribeRequestMessageType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentNotifyRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityDocumentSubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogEntityNotifyResponseType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinNotifyRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinSubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogNhinUnsubscribeRequestType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogSubscribeResponseType;
import gov.hhs.fha.nhinc.common.hiemauditlog.LogUnsubscribeResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.SubscribeRequestType;
import gov.hhs.fha.nhinc.common.nhinccommoninternalorch.UnsubscribeRequestType;
import gov.hhs.fha.nhinc.nhinccomponentauditrepository.AuditRepositoryManagerService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.audit.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class AuditLogImpl {

    private static Log log = LogFactory.getLog(AuditLogImpl.class);
    static AuditRepositoryManagerService auditService = new AuditRepositoryManagerService();

    public AcknowledgementType logSubjectAdded(LogSubjectAddedRequestType logSubjectAddedRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = SubjectDiscoveryTransforms.transformPRPA2013012AuditMsg(logSubjectAddedRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logAdhocQuery(LogAdhocQueryRequestType logAdhocQueryRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = new DocumentQueryTransforms().transformDocQueryReq2AuditMsg(logAdhocQueryRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logSubjectRevised(LogSubjectRevisedRequestType logSubjectRevisedRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = SubjectDiscoveryTransforms.transformPRPA2013022AuditMsg(logSubjectRevisedRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logSubjectRevoked(LogSubjectRevokedRequestType logSubjectRevokedRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = SubjectDiscoveryTransforms.transformPRPA2013032AuditMsg(logSubjectRevokedRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logNhinSubjectDiscoveryAck(LogNhinSubjectDiscoveryAckRequestType logNhinSubjectDiscoveryAckRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = SubjectDiscoveryTransforms.transformAck2AuditMsg(logNhinSubjectDiscoveryAckRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logAdhocQueryResult(LogAdhocQueryResultRequestType logAdhocQueryResultRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = new DocumentQueryTransforms().transformDocQueryResp2AuditMsg(logAdhocQueryResultRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logDocRetrieve(LogDocRetrieveRequestType logDocRetrieveRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = DocumentRetrieveTransforms.transformDocRetrieveReq2AuditMsg(logDocRetrieveRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logDocRetrieveResult(LogDocRetrieveResultRequestType logDocRetrieveResultRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = DocumentRetrieveTransforms.transformDocRetrieveResp2AuditMsg(logDocRetrieveResultRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logFindAuditEvents(LogFindAuditEventsRequestType logFindAuditEventsRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = FindAuditEventsTransforms.transformFindAuditEventsReq2AuditMsg(logFindAuditEventsRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logFindAuditEventsResult(LogFindAuditEventsResultRequestType logFindAuditEventsResultRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            ack.setMessage("logFindAuditEventsResult method is not implemented");
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logSubjectReident(LogSubjectReidentificationRequestType logSubjectReidentRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = SubjectDiscoveryTransforms.transformPRPA2013092AuditMsg(logSubjectReidentRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logSubjectReidentResult(LogSubjectReidentificationResponseType logSubjectReidentResponse) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            LogEventRequestType auditMsg = SubjectDiscoveryTransforms.transformPRPA2013102AuditMsg(logSubjectReidentResponse);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public String logNhinSubscribeRequest(SubscribeRequestType message, String direction, String _interface) {
        String ack = null;

        if (isServiceEnabled()) {
            SubscribeTransforms transformLib = new SubscribeTransforms();
            LogNhinSubscribeRequestType reqMsg = new LogNhinSubscribeRequestType();
            reqMsg.setMessage(message);
            reqMsg.setDirection(direction);
            reqMsg.setInterface(_interface);

            LogEventRequestType auditMsg = transformLib.transformNhinSubscribeRequestToAuditMessage(reqMsg);
            ack = audit(auditMsg);
        } else {
            ack = NhincConstants.AUDIT_DISABLED_ACK_MSG;
        }
        return ack;
    }

    public String logNhinNotifyRequest(NotifyRequestType message, String direction, String _interface) {
        String ack = null;

        if (isServiceEnabled()) {
            NotifyTransforms transformLib = new NotifyTransforms();
            LogNhinNotifyRequestType reqMsg = new LogNhinNotifyRequestType();
            reqMsg.setMessage(message);
            reqMsg.setDirection(direction);
            reqMsg.setInterface(_interface);

            LogEventRequestType auditMsg = transformLib.transformNhinNotifyRequestToAuditMessage(reqMsg);
            ack = audit(auditMsg);
        } else {
            ack = NhincConstants.AUDIT_DISABLED_ACK_MSG;
        }
        return ack;
    }

    public String logNhinUnsubscribeRequest(UnsubscribeRequestType message, String direction, String _interface) {
        String ack = null;

        if (isServiceEnabled()) {
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();
            LogNhinUnsubscribeRequestType reqMsg = new LogNhinUnsubscribeRequestType();
            reqMsg.setMessage(message);
            reqMsg.setDirection(direction);
            reqMsg.setInterface(_interface);

            LogEventRequestType auditMsg = transformLib.transformNhinUnsubscribeRequestToAuditMessage(reqMsg);
            ack = audit(auditMsg);
        } else {
            ack = NhincConstants.AUDIT_DISABLED_ACK_MSG;
        }
        return ack;
    }

    public AcknowledgementType logUnsubscribeRespRequest(LogUnsubscribeResponseType logUnsubscribeRespRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            UnsubscribeTransforms transformLib = new UnsubscribeTransforms();

            LogEventRequestType auditMsg = transformLib.transformUnsubscribeResponseToGenericAudit(logUnsubscribeRespRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logSubscribeRespRequest(LogSubscribeResponseType logSubscribeRespRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            SubscribeTransforms transformLib = new SubscribeTransforms();

            LogEventRequestType auditMsg = transformLib.transformSubscribeResponseToAuditMessage(logSubscribeRespRequest);
            ack.setMessage(audit(auditMsg));
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public AcknowledgementType logEntityDocSubscribeRequest(LogEntityDocumentSubscribeRequestType logmEntityDocSubscribeRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            ack.setMessage("logEntityDocSubscribeRequest method is not implemented");
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public String logEntityCdcSubscribeRequest(EntityCdcSubscribeRequestMessageType message, String direction, String _interface) {
        String ack = null;

        if (isServiceEnabled()) {
            ack = "logEntityCdcSubscribeRequest method is not implemented";
        } else {
            ack = NhincConstants.AUDIT_DISABLED_ACK_MSG;
        }
        return ack;
    }

    public AcknowledgementType logEntityDocNotifyRequest(LogEntityDocumentNotifyRequestType logEntityDocNotifyRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            ack.setMessage("logEntityDocNotifyRequest method is not implemented");
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public String logEntityCdcNotifyRequest(EntityCdcNotifyRequestMessageType message, String direction, String _interface) {
        String ack = null;

        if (isServiceEnabled()) {
            ack = "logEntityCdcNotifyRequest method is not implemented";
        } else {
            ack = NhincConstants.AUDIT_DISABLED_ACK_MSG;
        }
        return ack;
    }

    public AcknowledgementType logEntityNotifyRespRequest(LogEntityNotifyResponseType logEntityNotifyRespRequest) {
        AcknowledgementType ack = new AcknowledgementType();

        if (isServiceEnabled()) {
            ack.setMessage("logEntityNotifyRespRequest method is not implemented");
        } else {
            ack.setMessage(NhincConstants.AUDIT_DISABLED_ACK_MSG);
        }
        return ack;
    }

    public String logEntityUnsubscribeRequest(EntityUnsubscribeRequestMessageType message, String direction, String _interface) {
        String ack = null;

        if (isServiceEnabled()) {
            ack = "logEntityUnsubscribeRequest method is not implemented";
        } else {
            ack = NhincConstants.AUDIT_DISABLED_ACK_MSG;
        }
        return ack;
    }

    private boolean isServiceEnabled() {
        boolean serviceEnabled = false;
        try {
            serviceEnabled = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.AUDIT_LOG_SERVICE_PROPERTY);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + NhincConstants.AUDIT_LOG_SERVICE_PROPERTY + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return serviceEnabled;
    }

    private String audit(LogEventRequestType auditMsg) {
        AcknowledgementType ack = null;

        IAuditRepositoryProxy auditRepoProxy = AuditRepositoryProxyFactory.getAuditRepositoryProxy();

        ack = auditRepoProxy.logEvent(auditMsg);

        return ack.getMessage();
    }
}
