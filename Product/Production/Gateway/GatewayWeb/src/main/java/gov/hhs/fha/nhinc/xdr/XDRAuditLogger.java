/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author dunnek
 */
public class XDRAuditLogger {
    public AcknowledgementType auditNhinXDR (ProvideAndRegisterDocumentSetRequestType request, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if(auditLogMsg != null)
        {
            audit(auditLogMsg, assertion);
        }
        return ack;
    }
    public AcknowledgementType auditXDR(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion, String direction)
    {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logXDRReq(request, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
            
        }
        return ack;
    }
    public AcknowledgementType auditNhinXDRResponse (RegistryResponseType Response, AssertionType assertion, String direction) {
        AcknowledgementType ack = new AcknowledgementType ();

        // Set up the audit logging request message
        AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();
        LogEventRequestType auditLogMsg = auditLogger.logNhinXDRResponse(Response, assertion, direction);

        if(auditLogMsg != null)
        {
            if(auditLogMsg.getAuditMessage() != null)
            {
                audit(auditLogMsg, assertion);
            }
        }
        return ack;
    }
    private AcknowledgementType audit(LogEventRequestType auditLogMsg, AssertionType assertion)
    {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        
        return proxy.auditLog(auditLogMsg, assertion);
    }
}
