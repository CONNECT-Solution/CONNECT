/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.subdisc.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecured;
import gov.hhs.fha.nhinc.entitysubjectdiscoverysecured.EntitySubjectDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PIXConsumerPRPAIN201301UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201301UVSecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201302UVSecuredRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201304UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVRequestType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVResponseType;
import org.hl7.v3.PIXConsumerPRPAIN201309UVSecuredRequestType;

/**
 *
 * @author jhoppesc
 */
public class EntitySubjectDiscoveryImpl {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntitySubjectDiscoveryImpl.class);
    private static EntitySubjectDiscoverySecured service = new EntitySubjectDiscoverySecured();

    public MCCIIN000002UV01 pixConsumerPRPAIN201301UV(PIXConsumerPRPAIN201301UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            EntitySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201301UVSecuredRequestType body = new PIXConsumerPRPAIN201301UVSecuredRequestType();
            body.setPRPAIN201301UV02(request.getPRPAIN201301UV02());
            body.setNhinTargetCommunities(request.getNhinTargetCommunities());
            ack = port.pixConsumerPRPAIN201301UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send entity subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201302UV(PIXConsumerPRPAIN201302UVRequestType request) {
        MCCIIN000002UV01 ack = new MCCIIN000002UV01();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            EntitySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201302UVSecuredRequestType body = new PIXConsumerPRPAIN201302UVSecuredRequestType();
            body.setPRPAIN201302UV02(request.getPRPAIN201302UV02());
            body.setNhinTargetCommunities(request.getNhinTargetCommunities());
            ack = port.pixConsumerPRPAIN201302UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send entity subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return ack;
    }

    public MCCIIN000002UV01 pixConsumerPRPAIN201304UV(PIXConsumerPRPAIN201304UVRequestType request) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public PIXConsumerPRPAIN201309UVResponseType pixConsumerPRPAIN201309UV(PIXConsumerPRPAIN201309UVRequestType request) {
        PIXConsumerPRPAIN201309UVResponseType response = new PIXConsumerPRPAIN201309UVResponseType();

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_SUBJECT_DISCOVERY_SECURED_SERVICE_NAME);

            EntitySubjectDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.SUBJECT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            PIXConsumerPRPAIN201309UVSecuredRequestType body = new PIXConsumerPRPAIN201309UVSecuredRequestType();
            body.setPRPAIN201309UV02(request.getPRPAIN201309UV02());
            body.setNhinTargetCommunities(request.getNhinTargetCommunities());
            response = port.pixConsumerPRPAIN201309UV(body);
        }
        catch (Exception ex) {
            log.error("Failed to send entity subject discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }
        return response;
    }

    private EntitySubjectDiscoverySecuredPortType getPort(String url) {
        EntitySubjectDiscoverySecuredPortType port = service.getEntitySubjectDiscoverySecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }
}
