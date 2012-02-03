package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class WebServiceProxyHelperTimeoutTest extends
		AbstractWebServiceProxyHelpTest {


	WebServiceProxyHelperProperties oHelper;

	@Before
	public void before() throws PropertyAccessException {

			retryAttemptsExpectation(mockPropertyAccessor,
				Expectations.returnValue("5"));

		exceptionExpectation(mockPropertyAccessor,
				Expectations.returnValue("PropertyAccessException"));
		retryDelayExpectation(mockPropertyAccessor,
	
				Expectations.returnValue("300"));

	}
	
	/**
	 * Test the GetTimeout method happy path.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetTimeoutHappyPath() throws PropertyAccessException {
		
		timeoutExpectation(mockPropertyAccessor,
				Expectations.returnValue("300"));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		int iTimeout = oHelper.getTimeout();
		assertEquals("Timeout failed.", 300, iTimeout);
	}

	/**
	 * Test the GetTimeout method with PropertyAccessException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetTimeoutPropertyException() throws PropertyAccessException {
	
		timeoutExpectation(mockPropertyAccessor,
				Expectations.throwException(new PropertyAccessException(
						"Failed to retrieve property.")));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);


		int iTimeout = oHelper.getTimeout();
		assertEquals("getTimeout failed: ", 0, iTimeout);

	}

	/**
	 * Test the GetTimeout method with NumberFormatException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetTimeoutNumberFormatException() throws PropertyAccessException {
	
		timeoutExpectation(mockPropertyAccessor,
				Expectations.returnValue("NotANumber"));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		
		int iTimeout = oHelper.getTimeout();
		assertEquals("getTimeout failed: ", 0, iTimeout);

	}

}
