/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.entity.patientdiscovery.async.request.queue.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncreqqueue.EntityPatientDiscoverySecuredAsyncReqQueue;
import gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncreqqueue.EntityPatientDiscoverySecuredAsyncReqQueuePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoveryAsyncReqQueueWebServiceProxy implements EntityPatientDiscoveryAsyncReqQueueProxy {
    private static Log log = LogFactory.getLog(EntityPatientDiscoveryAsyncReqQueueWebServiceProxy.class);
    private static EntityPatientDiscoverySecuredAsyncReqQueue service = new EntityPatientDiscoverySecuredAsyncReqQueue();

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request) {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();
        RespondingGatewayPRPAIN201305UV02SecuredRequestType securedRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
        securedRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        securedRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());


        // Get the URL to the Entity Patient Discovery Async Req Queue
        String url = getUrl();

        if (NullChecker.isNotNullish(url) && (request != null))
        {
            EntityPatientDiscoverySecuredAsyncReqQueuePortType port = getPort(url, request.getAssertion());
            response = port.addPatientDiscoveryAsyncReq(securedRequest);
        }

        return response;
    }

    private EntityPatientDiscoverySecuredAsyncReqQueuePortType getPort(String url, AssertionType assertion)
    {
        EntityPatientDiscoverySecuredAsyncReqQueuePortType port = service.getEntityPatientDiscoverySecuredAsyncReqQueuePortSoap();

        log.info("Setting endpoint address to Entity Patient Discovery Async Request Secured Queue Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        SamlTokenCreator tokenCreator = new SamlTokenCreator();
        Map samlMap = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PATIENT_DISCOVERY_ACTION);

        Map requestContext = ((BindingProvider) port).getRequestContext();
        requestContext.putAll(samlMap);

        return port;
    }

    private String getUrl() {
        String url = null;

        try
        {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_REQ_QUEUE_SERVICE_NAME);
        } catch (ConnectionManagerException ex)
        {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_DISCOVERY_ENTITY_SECURED_ASYNC_REQ_QUEUE_SERVICE_NAME + " for local home community");
            log.error(ex.getMessage());
        }

        return url;
    }

}
