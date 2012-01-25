package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;

public interface EntityPatientDiscoveryDeferredResponseOrch {

	public abstract MCCIIN000002UV01 processPatientDiscoveryAsyncRespOrch(PRPAIN201306UV02 body,
			AssertionType assertion, NhinTargetCommunitiesType target);

}
