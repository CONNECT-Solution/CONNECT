package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PatientCorrelationServiceSecuredFactoryTest {

	
	@Test
	public void getInstance() {
		assertNotNull(PatientCorrelationServiceSecuredFactory.getInstance());
	}
	
	@Test
	public void createPatientCorrelationService() {	
		PatientCorrelationOrch orchestration = null;
		PatientCorrelationServiceSecuredFactory factoryUnderTest = new PatientCorrelationServiceSecuredFactory(orchestration);
		assertTrue(factoryUnderTest.createPatientCorrelationService() instanceof PatientCorrelationServiceSecuredServiceImpl );
	}
}
