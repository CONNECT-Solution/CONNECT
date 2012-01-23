package gov.hhs.fha.nhinc.patientdiscovery.nhin;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryProcessor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.proxy.AdapterPatientDiscoveryProxy;
import gov.hhs.fha.nhinc.properties.ServicePropertyAccessor;

import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class NhinPatientDiscoveryOrchImplTest {

	Mockery context = new JUnit4Mockery();

	PatientDiscoveryAuditor auditor = context
			.mock(PatientDiscoveryAuditor.class);
	PatientDiscoveryProcessor mockProcessor = context
			.mock(PatientDiscoveryProcessor.class);
	AdapterPatientDiscoveryProxy mockProxy = context
			.mock(AdapterPatientDiscoveryProxy.class);

	GenericFactory<AdapterPatientDiscoveryProxy> proxyFactory = new GenericFactory<AdapterPatientDiscoveryProxy>() {
		@Override
		public AdapterPatientDiscoveryProxy create() {
			return mockProxy;
		}
	};

	@Test
	public void serviceNotEnabled() {

		NhinPatientDiscoveryOrchestration impl =buildOrchestrationForTest(false, false);
		PRPAIN201305UV02 body = null;
		AssertionType assertion = null;
		PRPAIN201306UV02 response = impl.respondingGatewayPRPAIN201305UV02(
				body, assertion);
		assertNull(response.getCreationTime());
	}

	@Test
	public void notPassThrough() {
		final PRPAIN201305UV02 body = new PRPAIN201305UV02();
		final AssertionType assertion = new AssertionType();
		final PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();

		NhinPatientDiscoveryOrchestration impl = buildOrchestrationForTest(true, false);

		context.checking(new Expectations() {
			{
				oneOf(auditor).auditAdapter201305(with(same(body)),
						with(same(assertion)), with("Outbound"));
				oneOf(mockProcessor).process201305(with(same(body)),
						with(same(assertion)));
				will(returnValue(expectedResponse));
				oneOf(auditor).auditAdapter201306(
						with(any(PRPAIN201306UV02.class)),
						with(same(assertion)), with("Inbound"));
			}
		});

		PRPAIN201306UV02 actualResponse = impl
				.respondingGatewayPRPAIN201305UV02(body, assertion);
		assertSame(expectedResponse, actualResponse);
	}
	
	@Test
	public void passThrough() {
		final PRPAIN201305UV02 body = new PRPAIN201305UV02();
		final AssertionType assertion = new AssertionType();
		final PRPAIN201306UV02 expectedResponse = new PRPAIN201306UV02();

		NhinPatientDiscoveryOrchestration impl = buildOrchestrationForTest(true, true);

		context.checking(new Expectations() {
			{
				oneOf(auditor).auditAdapter201305(with(same(body)),
						with(same(assertion)), with("Outbound"));
				oneOf(mockProxy).respondingGatewayPRPAIN201305UV02(with(same(body)),
						with(same(assertion)));
				will(returnValue(expectedResponse));
				oneOf(auditor).auditAdapter201306(
						with(any(PRPAIN201306UV02.class)),
						with(same(assertion)), with("Inbound"));
			}
		});

		PRPAIN201306UV02 actualResponse = impl
				.respondingGatewayPRPAIN201305UV02(body, assertion);
		assertSame(expectedResponse, actualResponse);
	}

	protected NhinPatientDiscoveryOrchestration buildOrchestrationForTest(final boolean serviceEnabled, final boolean passThrough) {
		NhinPatientDiscoveryOrchestration impl = new NhinPatientDiscoveryOrchImpl(
				new ServicePropertyAccessor() {

					@Override
					public boolean isServiceEnabled() {
						return serviceEnabled;
					}

					@Override
					public boolean isInPassThroughMode() {
						return passThrough;
					}
				}, auditor, mockProcessor, proxyFactory);
		return impl;
	}

}
