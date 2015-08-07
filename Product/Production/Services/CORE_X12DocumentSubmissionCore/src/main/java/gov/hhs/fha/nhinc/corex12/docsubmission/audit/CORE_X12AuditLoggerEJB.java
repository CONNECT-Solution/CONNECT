/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.corex12.docsubmission.audit;

import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform.CORE_X12AuditDataTransform;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Date;
import java.util.Properties;
import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.log4j.Logger;

/**
 *
 * @author nsubrama
 */
@Stateless
@LocalBean
public class CORE_X12AuditLoggerEJB {

    private static final Logger LOG = Logger.getLogger(CORE_X12AuditLoggerEJB.class);

    private final CORE_X12AuditDataTransform coreX12AuditDataTransform = new CORE_X12AuditDataTransform();

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     */
    @Asynchronous
    public void auditNhinCoreX12RealtimeMessage(Object message, AssertionType assertion, NhinTargetSystemType target,
        String direction, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.info("Inside the method CORE_X12AuditLoggerEJB.auditNhinCoreX12RealtimeMessage (START)-->TimeStamp:" + (new Date()));
        LOG.trace("---Begin CORE_X12AuditLoggerEJB.auditNhinCoreX12RealtimeMessage()---");
        // Set up the audit logging request message
        LogEventRequestType auditLogMsg = coreX12AuditDataTransform.transformX12MsgToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, isRequesting, webContextProperties, serviceName);
        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        } else {
            LOG.error("Core X12 Realtime Request auditLogMsg is null");
        }
        LOG.trace("---End CORE_X12AuditLoggerEJB.auditNhinCoreX12RealtimeMessage()---");
        LOG.info("Inside the method CORE_X12AuditLoggerEJB.auditNhinCoreX12RealtimeMessage (END)-->TimeStamp:" + (new Date()));
    }

    /**
     *
     * @param message
     * @param assertion
     * @param target
     * @param direction
     * @param isRequesting
     * @param webContextProperties
     * @param serviceName
     */
    @Asynchronous
    public void auditNhinCoreX12BatchMessage(Object message, AssertionType assertion, NhinTargetSystemType target,
        String direction, boolean isRequesting, Properties webContextProperties, String serviceName) {
        LOG.trace("---Begin CORE_X12AuditLoggerEJB.auditNhinCoreX12BatchRequest()---");
        LogEventRequestType auditLogMsg = coreX12AuditDataTransform.transformX12MsgToAuditMsg(message, assertion, target, direction, NhincConstants.AUDIT_LOG_NHIN_INTERFACE, isRequesting, webContextProperties, serviceName);
        if (auditLogMsg != null && auditLogMsg.getAuditMessage() != null) {
            audit(auditLogMsg, assertion);
        } else {
            LOG.error("Core X12 Nhin Batch Request auditLogMsg is null");
        }
        LOG.trace("---End CORE_X12AuditLoggerEJB.auditNhinCoreX12BatchRequest()---");
    }

    /**
     * Submits a generic Audit Log message to the Audit Log Repository.
     *
     * @param auditLogMsg The generic audit log to be audited
     * @param assertion The assertion to be audited
     * @return
     */
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion) {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy.auditLog(auditLogMsg, assertion);
    }

}
