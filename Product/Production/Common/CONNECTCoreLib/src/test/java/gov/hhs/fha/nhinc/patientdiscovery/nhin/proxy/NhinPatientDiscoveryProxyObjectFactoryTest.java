package gov.hhs.fha.nhinc.patientdiscovery.nhin.proxy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author Neil Webb
 */
@RunWith(JMock.class)
public class NhinPatientDiscoveryProxyObjectFactoryTest {
	Mockery context = new JUnit4Mockery();

	final Log mockLog = context.mock(Log.class);
	final NhinPatientDiscoveryProxy mockProxy = context
			.mock(NhinPatientDiscoveryProxy.class);
	
	@Test
	public void testGetNhinPatientDiscoveryProxyHappy() {
		final ApplicationContext mockContext = context
				.mock(ApplicationContext.class);
		NhinPatientDiscoveryProxyObjectFactory proxyFactory = new NhinPatientDiscoveryProxyObjectFactory() {
			@Override
			protected Log createLogger() {
				return mockLog;
			}

			@Override
			protected ApplicationContext getContext() {
				return mockContext;
			}
		};

		context.checking(new Expectations() {
			{
				oneOf(mockContext).getBean("nhinpatientdiscovery");
				will(returnValue(mockProxy));

			}
		});
		
		NhinPatientDiscoveryProxy actualProxy = proxyFactory
				.getNhinPatientDiscoveryProxy();
		assertNotNull("NhinPatientDiscoveryProxy was null", actualProxy);
		assertSame(mockProxy, actualProxy);
		
	}
	
}