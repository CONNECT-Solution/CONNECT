package gov.hhs.fha.nhinc.patientdiscovery.passthru.deferred.response.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

/**
 *
 * @author JHOPPESC
 */
public interface PassthruPatientDiscoveryDeferredRespProxy {

    public MCCIIN000002UV01 proxyProcessPatientDiscoveryAsyncResp(PRPAIN201306UV02 request, AssertionType assertion, NhinTargetSystemType targetSystem);

}
