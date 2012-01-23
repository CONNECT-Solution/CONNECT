package gov.hhs.fha.nhinc.patientdiscovery;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

public interface PatientDiscoveryProcessor {

	/**
	 * process201305
	 * @param request
	 * @param assertion
	 * @return org.hl7.PRPAIN201306UV02
	 */
	public PRPAIN201306UV02 process201305(PRPAIN201305UV02 request,
			AssertionType assertion);

}