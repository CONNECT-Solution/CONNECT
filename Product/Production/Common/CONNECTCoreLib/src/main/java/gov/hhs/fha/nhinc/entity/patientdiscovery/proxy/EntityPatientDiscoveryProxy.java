package gov.hhs.fha.nhinc.entity.patientdiscovery.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

/**
 *
 * @author Neil Webb
 */
public interface EntityPatientDiscoveryProxy
{
    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 pdRequest, AssertionType assertion, NhinTargetCommunitiesType targetCommunities);
}
