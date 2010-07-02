/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.nhinsubjectdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import ihe.iti.pixv3._2007.PIXConsumerPortType;
import ihe.iti.pixv3._2007.PIXConsumerService;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201302UV02;
import org.hl7.v3.PRPAIN201304UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.hl7.v3.PRPAIN201310UV02;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinSubjectDiscoveryWebServiceProxy implements NhinSubjectDiscoveryProxy {

    private static Log log = LogFactory.getLog(NhinSubjectDiscoveryWebServiceProxy.class);
    static PIXConsumerService nhinService = new PIXConsumerService();

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PRPAIN201301UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Nhin Subject Discovery Service
        url = getUrl(target);

        if (NullChecker.isNotNullish(url)) {
            PIXConsumerPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            ack = port.pixConsumerPRPAIN201301UV(request);

        } else {
            log.error("The URL for service: " + NhincConstants.SUBJECT_DISCOVERY_SERVICE_NAME + " is null");
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PRPAIN201302UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        // Get the URL to the Nhin Subject Discovery Service
        url = getUrl(target);

        if (NullChecker.isNotNullish(url)) {
            PIXConsumerPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            ack = port.pixConsumerPRPAIN201302UV(request);

        } else {
            log.error("The URL for service: " + NhincConstants.SUBJECT_DISCOVERY_SERVICE_NAME + " is null");
        }


        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PRPAIN201304UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PRPAIN201310UV02 pixConsumerPRPAIN201309UV(PRPAIN201309UV02 request, AssertionType assertion, NhinTargetSystemType target) {
        String url = null;
        PRPAIN201310UV02 resp = new PRPAIN201310UV02();

        // Get the URL to the Nhin Subject Discovery Service
        url = getUrl(target);

        if (NullChecker.isNotNullish(url)) {
            PIXConsumerPortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            resp = port.pixConsumerPRPAIN201309UV(request);

        } else {
            log.error("The URL for service: " + NhincConstants.SUBJECT_DISCOVERY_SERVICE_NAME + " is null");
        }


        return resp;
    }

    private PIXConsumerPortType getPort(String url) {
        PIXConsumerPortType port = nhinService.getPIXConsumerPortSoap();

        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);

        return port;
    }

    private String getUrl(NhinTargetSystemType target) {
        String url = null;

        if (target != null) {
            try {
                url = ConnectionManagerCache.getEndpontURLFromNhinTarget(target, NhincConstants.SUBJECT_DISCOVERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.SUBJECT_DISCOVERY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }
}
