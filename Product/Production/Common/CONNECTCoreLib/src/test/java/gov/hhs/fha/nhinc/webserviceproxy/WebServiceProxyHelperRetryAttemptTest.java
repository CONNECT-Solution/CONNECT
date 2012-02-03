package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class WebServiceProxyHelperRetryAttemptTest extends
		AbstractWebServiceProxyHelpTest {

	WebServiceProxyHelperProperties oHelper;

	@Before
	public void before() throws PropertyAccessException {
		retryDelayExpectation(mockPropertyAccessor,
				Expectations.returnValue("1"));

		timeoutExpectation(mockPropertyAccessor,
				Expectations.returnValue("300"));

		exceptionExpectation(mockPropertyAccessor,
				Expectations.returnValue("PropertyAccessException"));

	}

	/**
	 * Test the GetRetryAttempts method happy path.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetRetryAttemptsHappyPath() throws PropertyAccessException {

		retryAttemptsExpectation(mockPropertyAccessor,
				Expectations.returnValue("5"));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		int iRetryAttempts = oHelper.getRetryAttempts();
		assertEquals("RetryAttempts failed.", 5, iRetryAttempts);

	}

	/**
	 * Test the GetRetryAttempts method with PropertyAccessException.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetRetryAttemptsPropertyException()
			throws PropertyAccessException {

		retryAttemptsExpectation(mockPropertyAccessor,
				Expectations.throwException(new PropertyAccessException(
						"Failed to retrieve property.")));

		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		int iRetryAttempts = oHelper.getRetryAttempts();
		assertEquals("getRetryAttempts failed: ", 0, iRetryAttempts);

	}

	/**
	 * Test the GetRetryAttempts method with NumberFormatException.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetRetryAttemptsNumberFormatException()
			throws PropertyAccessException {
		retryAttemptsExpectation(mockPropertyAccessor,
				Expectations.returnValue("NotANumber"));
		
		oHelper = new WebServiceProxyHelperProperties(mockPropertyAccessor);

		int iRetryAttempts = oHelper.getRetryAttempts();
		assertEquals("getRetryAttempts failed: ", 0, iRetryAttempts);

	}

}
