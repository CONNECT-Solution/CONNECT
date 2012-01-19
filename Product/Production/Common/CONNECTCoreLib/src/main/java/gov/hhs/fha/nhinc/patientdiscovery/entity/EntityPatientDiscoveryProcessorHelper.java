package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Helper methods for PD Processing to create a new cumulativeResponse object
 * for a particular spec level and to transform an individualResponse object
 * from one spec to another
 * @author paul.eftis
 */
public class EntityPatientDiscoveryProcessorHelper{

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryProcessorHelper.class);


    /**
     * constructs a new EntityPatientDiscoveryOrchestratable_a0 object with
     * associated new cumulativeResponse
     * @param request
     * @return EntityPatientDiscoveryOrchestratable_a0
     */
    public static EntityPatientDiscoveryOrchestratable_a0 createNewCumulativeResponse_a0(
            EntityPatientDiscoveryOrchestratable request){

        EntityPatientDiscoveryOrchestratable_a0 cumulativeResponse = new EntityPatientDiscoveryOrchestratable_a0(
            null, null, null, null, request.getAssertion(),
            request.getServiceName(), request.getTarget(),
            request.getRequest());

        // create new cumulativeResponse object
        RespondingGatewayPRPAIN201306UV02ResponseType newResponse =
                new RespondingGatewayPRPAIN201306UV02ResponseType();
        cumulativeResponse.setCumulativeResponse(newResponse);
        log.debug("EntityPatientDiscoveryProcessorHelper constructed initial a0 cumulativeResponse");
        return cumulativeResponse;
    }


    /**
     * constructs a new EntityPatientDiscoveryOrchestratable_a1 object with
     * associated new cumulativeResponse
     * @param request
     * @return EntityPatientDiscoveryOrchestratable_a1
     */
    public static EntityPatientDiscoveryOrchestratable_a1 createNewCumulativeResponse_a1(
            EntityPatientDiscoveryOrchestratable request){

        EntityPatientDiscoveryOrchestratable_a1 cumulativeResponse = new EntityPatientDiscoveryOrchestratable_a1(
            null, null, null, null, request.getAssertion(),
            request.getServiceName(), request.getTarget(),
            request.getRequest());

        // create new cumulativeResponse object
        RespondingGatewayPRPAIN201306UV02ResponseType newResponse =
                new RespondingGatewayPRPAIN201306UV02ResponseType();
        cumulativeResponse.setCumulativeResponse(newResponse);
        log.debug("EntityPatientDiscoveryProcessorHelper constructed initial a1 cumulativeResponse");
        return cumulativeResponse;
    }


    /**
     * takes a response spec a1 and converts to response spec a0
     * @param original is spec a1
     * @return EntityPatientDiscoveryOrchestratable_a0 with transformed a0 response
     */
    public static EntityPatientDiscoveryOrchestratable_a0 transformResponse_a0(
            EntityPatientDiscoveryOrchestratable original){

        // currently a0 is same as a1
        EntityPatientDiscoveryOrchestratable_a0 response_a0 =
                new EntityPatientDiscoveryOrchestratable_a0();
        EntityPatientDiscoveryOrchestratable_a1 original_a1 =
                (EntityPatientDiscoveryOrchestratable_a1)original;
        response_a0.setResponse(original_a1.getResponse());
        return response_a0;
    }

    
    /**
     * takes a response spec a0 and converts to response spec a1
     * Currently PD response is same for both specs
     * @param original is spec a0
     * @return EntityPatientDiscoveryOrchestratable_a1 with transformed a1 response
     */
    public static EntityPatientDiscoveryOrchestratable_a1 transformResponse_a1(
            EntityPatientDiscoveryOrchestratable original){

        // currently a0 is same as a1
        EntityPatientDiscoveryOrchestratable_a1 response_a1 =
                new EntityPatientDiscoveryOrchestratable_a1();
        EntityPatientDiscoveryOrchestratable_a0 original_a0 =
                (EntityPatientDiscoveryOrchestratable_a0)original;
        response_a1.setResponse(original_a0.getResponse());
        return response_a1;
    }
}
