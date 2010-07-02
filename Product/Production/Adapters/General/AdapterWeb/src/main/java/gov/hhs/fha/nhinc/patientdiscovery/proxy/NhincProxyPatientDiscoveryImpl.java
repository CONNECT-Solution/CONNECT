/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecured.NhincProxyPatientDiscoverySecured;
import gov.hhs.fha.nhinc.nhincproxypatientdiscoverysecured.NhincProxyPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.ProxyPRPAIN201305UVProxyRequestType;
import org.hl7.v3.ProxyPRPAIN201305UVProxySecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class NhincProxyPatientDiscoveryImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyPatientDiscoveryImpl.class);
    private static NhincProxyPatientDiscoverySecured service = new NhincProxyPatientDiscoverySecured();

    public PRPAIN201306UV02 proxyPRPAIN201305UV(ProxyPRPAIN201305UVProxyRequestType request) {
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_PATIENT_DISCOVERY_SECURED_SERVICE_NAME);

            NhincProxyPatientDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PATIENT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            ProxyPRPAIN201305UVProxySecuredRequestType body = new ProxyPRPAIN201305UVProxySecuredRequestType();
            body.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
            body.setNhinTargetSystem(request.getNhinTargetSystem());
            response = port.proxyPRPAIN201305UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send proxy patient discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private NhincProxyPatientDiscoverySecuredPortType getPort(String url) {
        NhincProxyPatientDiscoverySecuredPortType port = service.getNhincProxyPatientDiscoverySecuredPort();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }
}
