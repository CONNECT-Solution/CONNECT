/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.patientdiscovery.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecured;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecured.EntityPatientDiscoverySecuredPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 * This class forwards the patient discovery request on to the gateway entity
 * patient discovery service.
 * 
 * @author shawc
 */
public class EntityPatientDiscoveryImpl {
    private Log log = null;
    private static EntityPatientDiscoverySecured service = new EntityPatientDiscoverySecured();

    public EntityPatientDiscoveryImpl() {
        log = createLogger();
    }


    public org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(RespondingGatewayPRPAIN201305UV02RequestType request) {
        RespondingGatewayPRPAIN201306UV02ResponseType response = new RespondingGatewayPRPAIN201306UV02ResponseType();

        log.debug("Entering EntityPatientDiscoveryImpl.respondingGatewayPRPAIN201305UV02...");

        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ENTITY_PATIENT_DISCOVERY_SECURED_SERVICE_NAME);

            EntityPatientDiscoverySecuredPortType port = getPort(url);

            AssertionType assertIn = request.getAssertion();
            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertIn, url, NhincConstants.PATIENT_DISCOVERY_ACTION);
            ((BindingProvider) port).getRequestContext().putAll(requestContext);

//            //create the request to the secured entity patient discovery service
//            org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType respondingGatewayPRPAIN201305UV02Request =
//                    new org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType();
//
//            respondingGatewayPRPAIN201305UV02Request.setPRPAIN201305UV02(request.getPRPAIN201305UV02());
//            respondingGatewayPRPAIN201305UV02Request.setAssertion(request.getAssertion());
//            respondingGatewayPRPAIN201305UV02Request.setNhinTargetCommunities(request.getNhinTargetCommunities());

            response = port.respondingGatewayPRPAIN201305UV02(request);
            
        }
        catch (Exception ex) {
            log.error("Failed to send proxy patient discovery from proxy EJB to secure interface: " + ex.getMessage(), ex);
        }

        return response;
    }

    private EntityPatientDiscoverySecuredPortType getPort(String url) {
        EntityPatientDiscoverySecuredPortType port = service.getEntityPatientDiscoverySecuredPortSoap11();
        gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) port, url);
        return port;
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
}
