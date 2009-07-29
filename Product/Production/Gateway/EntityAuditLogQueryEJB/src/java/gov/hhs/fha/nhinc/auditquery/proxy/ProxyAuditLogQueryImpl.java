/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.auditquery.proxy;

import com.services.nhinc.schema.auditmessage.FindAuditEventsResponseType;
import gov.hhs.fha.nhinc.auditquery.EntityAuditLog;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.FindAuditEventsRequestType;
import gov.hhs.fha.nhinc.nhinauditquery.proxy.NhinAuditQueryProxy;
import gov.hhs.fha.nhinc.nhinauditquery.proxy.NhinAuditQueryProxyObjectFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class ProxyAuditLogQueryImpl {

    private static Log log = LogFactory.getLog(ProxyAuditLogQueryImpl.class);

    /**
     * This method will perform an audit log query to a specified community on the Nhin Interface
     * and return a list of audit log records will be returned to the user.
     *
     * @param findAuditEventsRequest The audit log query search criteria
     * @return A list of Audit Log records that match the specified criteria
     */
    public FindAuditEventsResponseType findAuditEvents(FindAuditEventsRequestType findAuditEventsRequest) {
        log.debug("Entering ProxyAuditLogQueryImpl.findAuditEvents...");      

        // Audit the Audit Log Query Request Message sent on the Nhin Interface
        EntityAuditLog auditLog = new EntityAuditLog();
        AcknowledgementType ack = auditLog.audit(findAuditEventsRequest);

        NhinAuditQueryProxyObjectFactory auditFactory = new NhinAuditQueryProxyObjectFactory();
        NhinAuditQueryProxy proxy = auditFactory.getNhinAuditQueryProxy();

        log.debug("Exiting ProxyAuditLogQueryImpl.findAuditEvents...");
        return proxy.auditQuery(findAuditEventsRequest);
    }

}
