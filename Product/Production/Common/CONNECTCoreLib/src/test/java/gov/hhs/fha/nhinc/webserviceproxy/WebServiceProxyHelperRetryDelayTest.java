package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class WebServiceProxyHelperRetryDelayTest extends
		AbstractWebServiceProxyHelpTest {

	WebServiceProxyHelperProperties oHelper;

	@Before
	public void before() throws PropertyAccessException {

		timeoutExpectation(mockPropertyAccessor,
				Expectations.returnValue("300"));

		retryAttemptsExpectation(mockPropertyAccessor,
				Expectations.returnValue("5"));

		exceptionExpectation(mockPropertyAccessor,
				Expectations.returnValue("PropertyAccessException"));

	}

	/**
	 * Test the GetRetryDelay method happy path.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetRetryDelayHappyPath() throws PropertyAccessException {
		retryDelayExpectation(mockPropertyAccessor,
				Expectations.returnValue("300"));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		int iRetryDelay = oHelper.getRetryDelay();
		assertEquals("RetryDelay failed.", 300, iRetryDelay);

	}

	/**
	 * Test the GetRetryDelay method with PropertyAccessException.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetRetryDelayPropertyException()
			throws PropertyAccessException {
	
		
		retryDelayExpectation(mockPropertyAccessor,
				Expectations.throwException(new PropertyAccessException(
						"Failed to retrieve property.")));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		
		int iRetryDelay = oHelper.getRetryDelay();
		assertEquals("getRetryDelay failed: ", 0, iRetryDelay);

	}

	/**
	 * Test the GetRetryDelay method with NumberFormatException.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetRetryDelayNumberFormatException()
			throws PropertyAccessException {
		
		
		retryDelayExpectation(mockPropertyAccessor,
				Expectations.returnValue("NotANumber"));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);


		int iRetryDelay = oHelper.getRetryDelay();
		assertEquals("getRetryDelay failed: ", 0, iRetryDelay);

	}
}
