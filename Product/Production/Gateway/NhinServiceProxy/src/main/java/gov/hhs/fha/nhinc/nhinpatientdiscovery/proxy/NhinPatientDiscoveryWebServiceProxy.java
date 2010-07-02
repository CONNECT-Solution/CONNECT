/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinpatientdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.xcpd._2009.RespondingGatewayPortType;
import ihe.iti.xcpd._2009.RespondingGatewayService;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author jhoppesc
 */
public class NhinPatientDiscoveryWebServiceProxy implements NhinPatientDiscoveryProxy {

    private static Log log = LogFactory.getLog(NhinPatientDiscoveryWebServiceProxy.class);
    static RespondingGatewayService nhinService = new RespondingGatewayService();

    public PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        PRPAIN201306UV02 response = new PRPAIN201306UV02();

        // Get the URL to the Nhin Subject Discovery Service
        url = getUrl(target);

        if (NullChecker.isNotNullish(url)) {
            RespondingGatewayPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            response = port.respondingGatewayPRPAIN201305UV02(request);

        } else {
            log.error("The URL for service: " + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME + " is null");
        }

        return response;
    }

    private RespondingGatewayPortType getPort(String url) {
        RespondingGatewayPortType port = nhinService.getRespondingGatewayPortSoap12();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }

    private String getUrl(NhinTargetSystemType target) {
        String url = null;

        if (target != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
}
