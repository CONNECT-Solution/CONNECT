package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

public interface PolicyChecker<OUTGOING, INCOMING> {

	public abstract boolean checkOutgoingPolicy(OUTGOING request);

	public abstract boolean checkIncomingPolicy(INCOMING request, AssertionType assertion);

}
