package gov.hhs.fha.nhinc.patientcorrelation.nhinc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;

import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredRequestType;
import org.hl7.v3.RetrievePatientCorrelationsSecuredResponseType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;

public class PatientCorrelationServiceSecuredTest {
	Mockery context = new JUnit4Mockery();
	
	@SuppressWarnings("unchecked")
	final
	PatientCorrelationServiceFactory<RetrievePatientCorrelationsSecuredRequestType, 
	RetrievePatientCorrelationsSecuredResponseType, 
	AddPatientCorrelationSecuredRequestType, 
	AddPatientCorrelationSecuredResponseType> mockFactory = (PatientCorrelationServiceFactory<RetrievePatientCorrelationsSecuredRequestType,
			RetrievePatientCorrelationsSecuredResponseType, 
			AddPatientCorrelationSecuredRequestType,
			AddPatientCorrelationSecuredResponseType>) context
			.mock(PatientCorrelationServiceFactory.class);
	@SuppressWarnings("unchecked")
	final
	PatientCorrelationService<RetrievePatientCorrelationsSecuredRequestType,
	RetrievePatientCorrelationsSecuredResponseType, 
	AddPatientCorrelationSecuredRequestType,
	AddPatientCorrelationSecuredResponseType> mockService = (PatientCorrelationService<RetrievePatientCorrelationsSecuredRequestType,
			RetrievePatientCorrelationsSecuredResponseType, 
			AddPatientCorrelationSecuredRequestType,
			AddPatientCorrelationSecuredResponseType>)context.mock(PatientCorrelationService.class);

	@Test
	public void defaultConstructor() {
		assertNotNull(new PatientCorrelationServiceSecured());
	}

	@Test
	public void addPatientCorrelation() {
		
		final AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
		final AddPatientCorrelationSecuredResponseType expectedResponse = new AddPatientCorrelationSecuredResponseType();
		
		context.checking(new Expectations() {{
			
			oneOf(mockFactory).createPatientCorrelationService();
			will(returnValue(mockService));
			
			oneOf(mockService).addPatientCorrelation(with(same(request)), with(any(AssertionType.class)));
			will(returnValue(expectedResponse));
			
		}});
		
		PatientCorrelationServiceSecured serviceUnderTest = new PatientCorrelationServiceSecured(mockFactory);
		
		AddPatientCorrelationSecuredResponseType actualResponse = serviceUnderTest.addPatientCorrelation(request);
		assertSame(expectedResponse, actualResponse);
	}
	
	@Test
	public void retrievePatientCorrelation() {
		
		final RetrievePatientCorrelationsSecuredRequestType request = new RetrievePatientCorrelationsSecuredRequestType();
		final RetrievePatientCorrelationsSecuredResponseType expectedResponse = new RetrievePatientCorrelationsSecuredResponseType();
		
		context.checking(new Expectations() {{
			
			oneOf(mockFactory).createPatientCorrelationService();
			will(returnValue(mockService));
			
			oneOf(mockService).retrievePatientCorrelations(with(same(request)), with(any(AssertionType.class)));
			will(returnValue(expectedResponse));
			
		}});
		
		PatientCorrelationServiceSecured serviceUnderTest = new PatientCorrelationServiceSecured(mockFactory);
		
		RetrievePatientCorrelationsSecuredResponseType actualResponse = serviceUnderTest.retrievePatientCorrelations(request);
		assertSame(expectedResponse, actualResponse);
	}
}
