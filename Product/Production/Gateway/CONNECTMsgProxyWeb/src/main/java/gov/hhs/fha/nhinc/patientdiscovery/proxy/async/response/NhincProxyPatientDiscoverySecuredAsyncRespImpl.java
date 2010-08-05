/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy.async.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy.NhinPatientDiscoveryAsyncRespProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.async.response.proxy.NhinPatientDiscoveryAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.xml.ws.WebServiceContext;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.ProxyPRPAIN201306UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201306UVProxySecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class NhincProxyPatientDiscoverySecuredAsyncRespImpl {
    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxySecuredRequestType request, WebServiceContext context) {
        ProxyPRPAIN201306UVProxyRequestType unsecureRequest = new ProxyPRPAIN201306UVProxyRequestType();
        unsecureRequest.setNhinTargetSystem(request.getNhinTargetSystem());
        unsecureRequest.setPRPAIN201306UV02(request.getPRPAIN201306UV02());
        unsecureRequest.setAssertion(SamlTokenExtractor.GetAssertion(context));
        
        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                unsecureRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            unsecureRequest.getAssertion().setMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        MCCIIN000002UV01 ack = proxyProcessPatientDiscoveryAsyncResp(unsecureRequest);

        return ack;
    }

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(ProxyPRPAIN201306UVProxyRequestType request) {
       MCCIIN000002UV01 resp = new MCCIIN000002UV01();

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLogger auditLog = new PatientDiscoveryAuditLogger();
        AcknowledgementType ack = auditLog.auditNhin201306(request.getPRPAIN201306UV02(), request.getAssertion(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);

        NhinPatientDiscoveryAsyncRespProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryAsyncRespProxyObjectFactory();
        NhinPatientDiscoveryAsyncRespProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryAsyncRespProxy();

        resp = proxy.respondingGatewayPRPAIN201306UV02(request.getPRPAIN201306UV02(), request.getAssertion(), request.getNhinTargetSystem());

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        ack = auditLog.auditAck(resp, request.getAssertion(), NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        return resp;
    }

}
