package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;

public interface NhinPatientDiscoveryOrchestration {

	public abstract PRPAIN201306UV02 respondingGatewayPRPAIN201305UV02(
			PRPAIN201305UV02 body, AssertionType assertion);

}