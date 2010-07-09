/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.audit.PatientDiscoveryTransforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
/**
 *
 * @author dunnek
 */
public class XDRAuditLog {
    private Log log = null;

    public XDRAuditLog()
    {
        log = createLogger();
    }


    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    public AcknowledgementType auditProxyRequest(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {
         AcknowledgementType ack = new AcknowledgementType();
        if (request == null)
        {
            log.error("Unable to create an audit log record for the proxy. The incomming request was null.");
        }
        else if (assertion == null)
        {
            log.error("Unable to create an audit log record for the proxy. The incomming request assertion was null.");
        }
        else
        {
//            PatientDiscoveryTransforms oPatientDiscoveryTransforms = new PatientDiscoveryTransforms();
            LogEventRequestType message = getLogEventRequestTypeForProxyRequestMessage( request,assertion);
            ack = logXDRRequest(message, assertion);
        }
        return ack;
    }
    public AcknowledgementType auditProxyResponse(RegistryResponseType response, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();
        if (response == null)
        {
            log.error("Unable to create an audit log record for the proxy. The incomming response was null.");
        }
        else if (assertion == null)
        {
            log.error("Unable to create an audit log record for the proxy. The incomming response assertion was null.");
        }
        else
        {
            LogEventRequestType message = getLogEventRequestTypeForProxyResponseMessage(response, assertion);
            ack = logXDRResponse(message, assertion);
        }
        return ack;
    }

    public AcknowledgementType logXDRRequest (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest == null)
        {
            log.error("There was a problem creating an audit log for the request (LogEventRequestType parameter was null). The audit record was not created.");
        }
        else if(assertion == null)
        {
            log.error("There was a problem creating an audit log for the request (AssertionType parameter was null). The audit record was not created.");
        }
        else
        {
            AuditRepositoryProxyObjectFactory auditRepoFactory = getAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = getAuditRepositoryProxy(auditRepoFactory);
            ack = getAuditLogProxyResponse(proxy, auditLogRequest, assertion);
        }

        return ack;
    }
    public AcknowledgementType logXDRResponse (LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack = new AcknowledgementType();

        if (auditLogRequest == null)
        {
            log.error("There was a problem creating an audit log for the response (LogEventRequestType parameter was null). The audit record was not created.");
        }
        else if (assertion == null)
        {
            log.error("There was a problem creating an audit log for the response (AssertionType parameter was null). The audit record was not created.");
        }
        else
        {
            AuditRepositoryProxyObjectFactory auditRepoFactory = getAuditRepositoryProxyObjectFactory();
            AuditRepositoryProxy proxy = getAuditRepositoryProxy(auditRepoFactory);
            log.debug("Calling the audit log proxy to create the audit log.");
            ack = getAuditLogProxyResponse(proxy, auditLogRequest, assertion);
        }

        return ack;
    }

    protected LogEventRequestType getLogEventRequestTypeForProxyRequestMessage(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request, AssertionType assertion) {

        LogEventRequestType message = new LogEventRequestType();

        return message;
    }
    protected AcknowledgementType getAuditLogProxyResponse(AuditRepositoryProxy proxy, LogEventRequestType auditLogRequest, AssertionType assertion) {
        AcknowledgementType ack;
        ack = proxy.auditLog(auditLogRequest, assertion);
        return ack;
    }

    protected LogEventRequestType getLogEventRequestTypeForProxyResponseMessage(RegistryResponseType response, AssertionType assertion) {
        //LogEventRequestType message = new PatientDiscoveryTransforms().transformNhinXDRResponseToAuditMsg(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);
        LogEventRequestType message = new LogEventRequestType();
        return message;
    }

    protected AuditRepositoryProxy getAuditRepositoryProxy(AuditRepositoryProxyObjectFactory auditRepoFactory) {
        AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
        return proxy;
    }

    protected AuditRepositoryProxyObjectFactory getAuditRepositoryProxyObjectFactory() {
        AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
        return auditRepoFactory;
    }
}
