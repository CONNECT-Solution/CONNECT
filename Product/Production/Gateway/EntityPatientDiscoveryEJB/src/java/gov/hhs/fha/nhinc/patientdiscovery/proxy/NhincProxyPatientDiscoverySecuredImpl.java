/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.proxy.NhinPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.nhinpatientdiscovery.proxy.NhinPatientDiscoveryProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLog;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.properties.*;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoverySecuredImpl {
    private static Log log = LogFactory.getLog(NhincProxyPatientDiscoverySecuredImpl.class);



    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxySecuredRequestType request, WebServiceContext context) {
        log.debug("Entering NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        // Audit the Patient Discovery Request Message sent on the Nhin Interface
        PatientDiscoveryAuditLog auditLog = new PatientDiscoveryAuditLog();
        AcknowledgementType ack = auditLog.auditProxyRequest(request);

        NhinPatientDiscoveryProxyObjectFactory patientDiscoveryFactory = new NhinPatientDiscoveryProxyObjectFactory();
        NhinPatientDiscoveryProxy proxy = patientDiscoveryFactory.getNhinPatientDiscoveryProxy();

        response = proxy.respondingGatewayPRPAIN201305UV02(request.getPRPAIN201305UV02(), assertion, request.getNhinTargetSystem());

        // Audit the Patient Discovery Response Message received on the Nhin Interface
        ack = auditLog.auditProxyResponse(response, assertion);


        response = new ResponseFactory().getResponseMode().processResponse(response, context);
        
        log.debug("Exiting NhincProxyPatientDiscoverySecuredImpl.proxyPRPAIN201305UV...");
        return response;
    }


}
