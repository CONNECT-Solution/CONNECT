package gov.hhs.fha.nhinc.patientdiscovery.nhin.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;

public interface NhinPatientDiscoveryDeferredReqOrch {

	public abstract MCCIIN000002UV01 respondingGatewayPRPAIN201305UV02(PRPAIN201305UV02 request,
			AssertionType assertion);

}
