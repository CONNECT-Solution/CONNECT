package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class EntityPatientDiscoveryDeferredRequestProxyNoOpImpl implements EntityPatientDiscoveryDeferredRequestProxy {

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetCommunitiesType target)
    {
        return new MCCIIN000002UV01();
    }

}
