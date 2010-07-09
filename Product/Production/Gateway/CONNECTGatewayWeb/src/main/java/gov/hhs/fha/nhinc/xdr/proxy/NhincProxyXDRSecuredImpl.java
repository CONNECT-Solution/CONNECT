/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.xdr.XDRAuditLogger;

/**
 *
 * @author dunnek
 */
public class NhincProxyXDRSecuredImpl {
    private Log log = null;

    public NhincProxyXDRSecuredImpl()
    {
        log = createLogger();
    }
    public RegistryResponseType provideAndRegisterDocumentSetB(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context) {
        log.debug("begin provideAndRegisterDocumentSetB()");
        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        return provideAndRegisterDocumentSetB(body, assertion);
    }
    public RegistryResponseType provideAndRegisterDocumentSetB(RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, AssertionType assertion)
    {
        RegistryResponseType response = null;
        
        //TODO: LogRequest
        XDRAuditLogger auditLog = new XDRAuditLogger();
        AcknowledgementType ack = auditLog.auditXDR(body, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        log.debug("ack: " + ack.getMessage());

        NhinXDRProxyObjectFactory factory = new NhinXDRProxyObjectFactory();
        NhinXDRProxy proxy = factory.getNhinXDRProxy();

        response = proxy.provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion, body.getNhinTargetSystem());

        ack = auditLog.auditNhinXDRResponse(response, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        log.debug("ack: " + ack.getMessage());
        //TODO: Log Response
        return response;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
