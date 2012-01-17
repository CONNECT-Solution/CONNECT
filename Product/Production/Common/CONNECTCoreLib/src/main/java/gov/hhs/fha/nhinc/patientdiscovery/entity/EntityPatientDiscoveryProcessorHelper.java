package gov.hhs.fha.nhinc.patientdiscovery.entity;

import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author paul.eftis
 */
public class EntityPatientDiscoveryProcessorHelper{

    private static Log log = LogFactory.getLog(EntityPatientDiscoveryProcessorHelper.class);


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
        log.debug("EntityPatientDiscoveryProcessorHelper constructed initial cumulativeResponse");
        return cumulativeResponse;
    }



    // takes a response spec a1 and converts to response spec a0
    public static EntityPatientDiscoveryOrchestratable_a0 transformResponse_a0(
            EntityPatientDiscoveryOrchestratable original){

        EntityPatientDiscoveryOrchestratable_a0 response_a0 = new EntityPatientDiscoveryOrchestratable_a0();
        // currently a0 is same as a1
        // EntityDocQueryOrchestratable_a1 original_a1 = (EntityDocQueryOrchestratable_a1)original;
        EntityPatientDiscoveryOrchestratable_a0 original_a0 = (EntityPatientDiscoveryOrchestratable_a0)original;
        response_a0.setResponse(original_a0.getResponse());
        return response_a0;
    }

    
    // takes a response spec a0 and converts to response spec a1
    public static EntityPatientDiscoveryOrchestratable_a0 transformResponse_a1(
            EntityPatientDiscoveryOrchestratable original){

        // currently a0 is same as a1
        EntityPatientDiscoveryOrchestratable_a0 response_a1 = new EntityPatientDiscoveryOrchestratable_a0();
        EntityPatientDiscoveryOrchestratable_a0 original_a0 = (EntityPatientDiscoveryOrchestratable_a0)original;
        response_a1.setResponse(original_a0.getResponse());
        return response_a1;
    }
}
