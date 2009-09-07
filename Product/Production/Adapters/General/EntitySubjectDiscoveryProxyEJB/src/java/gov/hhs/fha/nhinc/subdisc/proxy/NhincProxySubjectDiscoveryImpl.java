/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.subdisc.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecured;
import gov.hhs.fha.nhinc.nhincproxysubjectdiscoverysecured.NhincProxySubjectDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVProxySecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201303UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201303UVProxySecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxyRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVProxySecuredRequestType;
import org.hl7.v3.PRPAIN201310UV;

/**
 *
 * @author jhoppesc
 */
public class NhincProxySubjectDiscoveryImpl {
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxySubjectDiscoveryImpl.class);
    private static NhincProxySubjectDiscoverySecured service = new NhincProxySubjectDiscoverySecured();

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVProxyRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            NhincProxySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201301UVProxySecuredRequestType body = new PIXConsumerPRPAIN201301UVProxySecuredRequestType();
            body.setPRPAIN201301UV(request.getPRPAIN201301UV());
            body.setNhinTargetSystem(request.getNhinTargetSystem());
            ack = port.pixConsumerPRPAIN201301UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send proxy subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201303UV(PIXConsumerPRPAIN201303UVProxyRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            NhincProxySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201303UVProxySecuredRequestType body = new PIXConsumerPRPAIN201303UVProxySecuredRequestType();
            body.setPRPAIN201303UV(request.getPRPAIN201303UV());
            body.setNhinTargetSystem(request.getNhinTargetSystem());
            ack = port.pixConsumerPRPAIN201303UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send proxy subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public PRPAIN201310UV pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVProxyRequestType request) {
        PRPAIN201310UV response = new PRPAIN201310UV();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHINC_PROXY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            NhincProxySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201309UVProxySecuredRequestType body = new PIXConsumerPRPAIN201309UVProxySecuredRequestType();
            body.setPRPAIN201309UV(request.getPRPAIN201309UV());
            body.setNhinTargetSystem(request.getNhinTargetSystem());
            response = port.pixConsumerPRPAIN201309UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send proxy subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private NhincProxySubjectDiscoverySecuredPortType getPort(String url) {
        NhincProxySubjectDiscoverySecuredPortType port = service.getNhincProxySubjectDiscoverySecuredPortSoap11();

        log.info("Setting endpoint address to Entity Subject Discovery Secured Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
}
