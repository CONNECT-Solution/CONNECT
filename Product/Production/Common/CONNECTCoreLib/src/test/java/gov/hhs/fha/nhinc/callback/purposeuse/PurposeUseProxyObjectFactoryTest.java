package gov.hhs.fha.nhinc.callback.purposeuse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PurposeUseProxyObjectFactoryTest {

	private PurposeUseProxyObjectFactory proxyFactory;
	
	@Before
	public void setUp() throws Exception {
		proxyFactory = new PurposeUseProxyObjectFactory(){
			@Override
			protected <T extends Object> T getBean(String beanName, Class<T> type){
				return type.cast(new PurposeUseProxyDefaultImpl());
			}			
		};
	}

	@Test
	public void testGetConfigFileName() {
		assertEquals(proxyFactory.getConfigFileName(), "PurposeUseProxyConfig.xml");
	}
	
	@Test
	public void testGetPurposeUseProxy() {
		PurposeUseProxy proxy = proxyFactory.getPurposeUseProxy();
		assertTrue(proxy instanceof PurposeUseProxyDefaultImpl);
	}

}
