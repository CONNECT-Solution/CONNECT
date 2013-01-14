package gov.hhs.fha.nhinc.patientcorrelation.nhinc.proxy;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClientFactory;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationPortType;

import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.PRPAIN201309UV02;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PatientCorrelationProxyWebServiceUnsecuredImplTest {
	
	private CONNECTClientFactory mockCONNECTClientFactory = mock(CONNECTClientFactory.class);
	@SuppressWarnings("unchecked")
	private CONNECTClient<PatientCorrelationPortType> mockClient = mock (CONNECTClient.class);
	@SuppressWarnings("unchecked")
	private CONNECTClient<PatientCorrelationPortType> mockSecuredClient = mock (CONNECTClient.class);
	private AssertionType mockAssertion = mock(AssertionType.class);
	private PRPAIN201301UV02 mock301 = mock(PRPAIN201301UV02.class);
	private PRPAIN201309UV02 mock309 = mock(PRPAIN201309UV02.class);
	
	
	@SuppressWarnings("unchecked")
	@Test
	public void testAddPatientCorrelation() throws Exception {
		PatientCorrelationProxyWebServiceUnsecuredImpl impl = getPatientCorrelationProxyWebServiceUnsecuredImpl();
		impl.addPatientCorrelation(mock301, mockAssertion);
		
		verify(mockClient).invokePort(any(Class.class), any(String.class), any(Object.class));
		verify(mockCONNECTClientFactory).getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class), any(AssertionType.class));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testretrievePatientCorrelations() throws Exception {
		PatientCorrelationProxyWebServiceUnsecuredImpl impl = getPatientCorrelationProxyWebServiceUnsecuredImpl();
		impl.retrievePatientCorrelations(mock309, mockAssertion);
		
		verify(mockClient).invokePort(any(Class.class), any(String.class), any(Object.class));
		verify(mockCONNECTClientFactory).getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class), any(AssertionType.class));
	}

	protected PatientCorrelationProxyWebServiceUnsecuredImpl getPatientCorrelationProxyWebServiceUnsecuredImpl() {
		return new PatientCorrelationProxyWebServiceUnsecuredImpl() {
			
			@Override
			protected CONNECTClientFactory getCONNECTClientFactory() {
				return mockCONNECTClientFactory;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		when(mockCONNECTClientFactory.getCONNECTClientUnsecured(any(ServicePortDescriptor.class), any(String.class), any(AssertionType.class))).thenReturn(mockClient);
		when(mockCONNECTClientFactory.getCONNECTClientSecured(any(ServicePortDescriptor.class), any(String.class), any(AssertionType.class))).thenReturn(mockSecuredClient);
	}
}
