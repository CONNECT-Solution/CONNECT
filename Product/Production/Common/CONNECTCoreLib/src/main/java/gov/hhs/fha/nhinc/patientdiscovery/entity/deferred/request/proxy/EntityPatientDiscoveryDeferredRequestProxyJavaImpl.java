package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestOrchImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class EntityPatientDiscoveryDeferredRequestProxyJavaImpl implements EntityPatientDiscoveryDeferredRequestProxy
{

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets)
    {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        EntityPatientDiscoveryDeferredRequestOrchImpl orchImpl = new EntityPatientDiscoveryDeferredRequestOrchImpl();
        response = orchImpl.processPatientDiscoveryAsyncReq(request, assertion, targets);

        return response;
    }
}
