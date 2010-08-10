package gov.hhs.fha.nhinc.patientdiscovery.entity.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import gov.hhs.fha.nhinc.patientdiscovery.entity.EntityPatientDiscoveryProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author Neil Webb
 */
public class EntityPatientDiscoveryProxyJavaImpl implements EntityPatientDiscoveryProxy
{
    private Log log = null;

    public EntityPatientDiscoveryProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    protected EntityPatientDiscoveryProcessor getEntityPatientDiscoveryProcessor()
    {
        return new EntityPatientDiscoveryProcessor();
    }

    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 pdRequest, AssertionType assertion, NhinTargetCommunitiesType targetCommunities)
    {
        log.debug("Begin respondingGatewayPRPAIN201305UV02");
        RespondingGatewayPRPAIN201306UV02ResponseType response = null;

        EntityPatientDiscoveryProcessor processor = getEntityPatientDiscoveryProcessor();
        if(processor == null)
        {
            log.warn("EntityPatientDiscoveryProcessor was null");
        }
        else
        {
            RespondingGatewayPRPAIN201305UV02RequestType processorRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
            processorRequest.setPRPAIN201305UV02(pdRequest);
            processorRequest.setAssertion(assertion);
            processorRequest.setNhinTargetCommunities(targetCommunities);
            response = processor.respondingGatewayPRPAIN201305UV02(processorRequest, assertion);
        }
        log.debug("End respondingGatewayPRPAIN201305UV02");
        return response;
    }

}
