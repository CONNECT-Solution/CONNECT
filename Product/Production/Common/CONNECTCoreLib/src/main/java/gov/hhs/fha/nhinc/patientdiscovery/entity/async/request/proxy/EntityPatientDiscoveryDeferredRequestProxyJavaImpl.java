package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestOrchImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;


public class EntityPatientDiscoveryDeferredRequestProxyJavaImpl implements EntityPatientDiscoveryDeferredRequestProxy
{

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(RespondingGatewayPRPAIN201305UV02RequestType request)
    {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        RespondingGatewayPRPAIN201305UV02SecuredRequestType securedRequest = new RespondingGatewayPRPAIN201305UV02SecuredRequestType();
        securedRequest.setNhinTargetCommunities(request.getNhinTargetCommunities());
        securedRequest.setPRPAIN201305UV02(request.getPRPAIN201305UV02());

        AssertionType assertion = request.getAssertion();

        EntityPatientDiscoveryDeferredRequestOrchImpl orchImpl = new EntityPatientDiscoveryDeferredRequestOrchImpl();
        response = orchImpl.processPatientDiscoveryAsyncReq(securedRequest, assertion);

        return response;
    }
}
