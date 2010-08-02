package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy ;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscovery;
import gov.hhs.fha.nhinc.entitypatientdiscovery.EntityPatientDiscoveryPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryProxyWebServiceUnsecuredImpl implements EntityPatientDiscoveryProxy
{
    private Log log = null;
    private static EntityPatientDiscovery patientDiscoveryService = null;

    public EntityPatientDiscoveryProxyWebServiceUnsecuredImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected String invokeConnectionManager(String serviceName) throws ConnectionManagerException
    {
        return ConnectionManagerCache.getLocalEndpointURLByServiceName(serviceName);
    }

    protected String getEndpointURL()
    {
        String endpointURL = null;
        String serviceName = NhincConstants.ENTITY_PATIENT_DISCOVERY_SERVICE_NAME;
        try
        {
            endpointURL = invokeConnectionManager(serviceName);
            log.debug("Retrieved endpoint URL for service " + serviceName + ": " + endpointURL);
        }
        catch (ConnectionManagerException ex)
        {
            log.error("Error getting url for " + serviceName + " from the connection manager. Error: " + ex.getMessage(), ex);
        }

        return endpointURL;
    }

    protected EntityPatientDiscovery getEntityPatientDiscovery()
    {
        if(patientDiscoveryService == null)
        {
            patientDiscoveryService = new EntityPatientDiscovery();
        }
        return patientDiscoveryService;
    }

    protected EntityPatientDiscoveryPortType getEntityPatientDiscoveryPortType()
    {
        EntityPatientDiscoveryPortType port = null;

        String endpointURL = getEndpointURL();

        if((endpointURL != null) && (!endpointURL.isEmpty()))
        {
            EntityPatientDiscovery service = getEntityPatientDiscovery();
            if(service != null)
            {
                port = service.getEntityPatientDiscoveryPortSoap();
                configurePort(port, endpointURL);
            }
            else
            {
                log.warn("EntityPatientDiscoverySecured was null");
            }
        }
        else
        {
            log.warn("Endpoint url was missing.");
        }

        return port;
    }

    protected void configurePort(EntityPatientDiscoveryPortType port, String endpointURL)
    {
        log.debug("Begin configurePort");
        if(port == null)
        {
            log.warn("configurePort - Port was null.");
        }
        else if (endpointURL == null)
        {
            log.warn("configurePort - Endpoint URL was null.");
        }
        else
        {
            Map requestContext = ((BindingProvider) port).getRequestContext();
            requestContext.put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        }
        log.debug("End configurePort");
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 pdRequest, AssertionType assertion, NhinTargetCommunitiesType targetCommunities)
    {
        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        try
        {
            EntityPatientDiscoveryPortType port = getEntityPatientDiscoveryPortType();

            if(pdRequest == null)
            {
                log.error("PRPAIN201305UV02 was null");
            }
            else if(assertion == null)
            {
                log.error("AssertionType was null");
            }
            else if(targetCommunities == null)
            {
                log.error("NhinTargetCommunitiesType was null");
            }
            else if(port == null)
            {
                log.error("EntityPatientDiscoverySecuredPortType was null");
            }
            else
            {
                RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
                request.setPRPAIN201305UV02(pdRequest);
                request.setAssertion(assertion);
                request.setNhinTargetCommunities(targetCommunities);

                response = port.respondingGatewayPRPAIN201305UV02(request);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling respondingGatewayPRPAIN201305UV02: " + ex.getMessage(), ex);
        }

        log.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

}
