package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.request.PassthruPatientDiscoveryDeferredRequestOrchImpl;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public class PassthruPatientDiscoveryDeferredRequestProxyJavaImpl implements PassthruPatientDiscoveryDeferredRequestProxy
{

    public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(PRPAIN201305UV02 message, AssertionType assertion, NhinTargetSystemType target)
    {
        MCCIIN000002UV01 response = new MCCIIN000002UV01();

        PassthruPatientDiscoveryDeferredRequestOrchImpl orchImpl = new PassthruPatientDiscoveryDeferredRequestOrchImpl();
        response = orchImpl.processPatientDiscoveryAsyncReq(message, assertion, target);

        return response;
    }
}
