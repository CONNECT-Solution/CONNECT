package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.AddPatientCorrelationRequestType;
import org.hl7.v3.AddPatientCorrelationResponseType;
import org.hl7.v3.RetrievePatientCorrelationsRequestType;
import org.hl7.v3.RetrievePatientCorrelationsResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

public class PatientCorrelationServiceUnsecuredTest {

Mockery context = new JUnit4Mockery();
	
	@SuppressWarnings("unchecked")
	final
	PatientCorrelationServiceFactory<RetrievePatientCorrelationsRequestType, 
	RetrievePatientCorrelationsResponseType, 
	AddPatientCorrelationRequestType, 
	AddPatientCorrelationResponseType> mockFactory = (PatientCorrelationServiceFactory<RetrievePatientCorrelationsRequestType,
			RetrievePatientCorrelationsResponseType, 
			AddPatientCorrelationRequestType,
			AddPatientCorrelationResponseType>) context
			.mock(PatientCorrelationServiceFactory.class);
	@SuppressWarnings("unchecked")
	final
	PatientCorrelationService<RetrievePatientCorrelationsRequestType, 
	RetrievePatientCorrelationsResponseType, 
	AddPatientCorrelationRequestType, 
	AddPatientCorrelationResponseType> mockService = (PatientCorrelationService<RetrievePatientCorrelationsRequestType, 
			RetrievePatientCorrelationsResponseType, 
			AddPatientCorrelationRequestType, 
			AddPatientCorrelationResponseType>)context.mock(PatientCorrelationService.class);

	
	
	@Test
	public void defaultConstructor() {
		assertNotNull(new PatientCorrelationServiceUnsecured());
	}
	
	@Test
	public void addPatientCorrelation() {
		final AddPatientCorrelationRequestType request = new AddPatientCorrelationRequestType();
		final AddPatientCorrelationResponseType expectedResponse = new AddPatientCorrelationResponseType();
		context.checking(new Expectations() {{
			
			oneOf(mockFactory).createPatientCorrelationService();
			will(returnValue(mockService));
			
			
			oneOf(mockService).addPatientCorrelation(with(same(request)), with(any(AssertionType.class)));
			will(returnValue(expectedResponse));
			
		}});
		
		PatientCorrelationServiceUnsecured serviceUnderTest = new PatientCorrelationServiceUnsecured(mockFactory);
		AddPatientCorrelationResponseType actualResponse = serviceUnderTest.addPatientCorrelation(request);
		assertSame(expectedResponse, actualResponse);
		
	}
	
	@Test
	public void retrievePatientCorrelation() {
		final RetrievePatientCorrelationsRequestType request = new RetrievePatientCorrelationsRequestType();
		final RetrievePatientCorrelationsResponseType expectedResponse = new RetrievePatientCorrelationsResponseType();
		context.checking(new Expectations() {{
			
			oneOf(mockFactory).createPatientCorrelationService();
			will(returnValue(mockService));
			
			
			oneOf(mockService).retrievePatientCorrelations(with(same(request)), with(any(AssertionType.class)));
			will(returnValue(expectedResponse));
			
		}});
		
		PatientCorrelationServiceUnsecured serviceUnderTest = new PatientCorrelationServiceUnsecured(mockFactory);
		RetrievePatientCorrelationsResponseType actualResponse = serviceUnderTest.retrievePatientCorrelations(request);
		assertSame(expectedResponse, actualResponse);
		
	}
	
	
}
