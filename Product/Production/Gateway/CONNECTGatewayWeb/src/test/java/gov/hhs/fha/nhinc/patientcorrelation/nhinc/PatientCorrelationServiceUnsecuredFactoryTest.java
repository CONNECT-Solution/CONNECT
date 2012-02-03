package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PatientCorrelationServiceUnsecuredFactoryTest {

	

	@Test
	public void getInstance() {
		assertNotNull(PatientCorrelationServiceUnsecuredFactory.getInstance());
	}
	
	@Test
	public void createPatientCorrelationService() {	
		PatientCorrelationServiceUnsecuredFactory factoryUnderTest = new PatientCorrelationServiceUnsecuredFactory(null);
		assertTrue(factoryUnderTest.createPatientCorrelationService() instanceof PatientCorrelationServiceUnsecuredImpl );
	}
}
