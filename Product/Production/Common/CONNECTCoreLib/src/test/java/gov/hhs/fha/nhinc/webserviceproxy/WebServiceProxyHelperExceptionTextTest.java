package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.IPropertyAcessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class WebServiceProxyHelperExceptionTextTest extends
		AbstractWebServiceProxyHelpTest {

	/**
	 * Test the GetExceptionText method happy path.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetExceptionTextHappyPath() throws PropertyAccessException {
		retryDelayExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

		retryAttemptsExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

		timeoutExpectation(mockPropertyAccessor, Expectations.returnValue("1"));
		
		exceptionExpectation(mockPropertyAccessor, Expectations.returnValue("PropertyAccessException"));
	

		WebServiceProxyHelperProperties properties = new WebServiceProxyHelperProperties(
				mockPropertyAccessor);

		String sExceptionText = properties.getExceptionText();
		assertEquals("getExceptionText failed.", "PropertyAccessException",
				sExceptionText);

	}

	
	/**
	 * Test the GetExceptionText method with PropertyAccessException.
	 * 
	 * @throws PropertyAccessException
	 */
	@Ignore
	@Test
	public void testGetExceptionTextPropertyException()
			throws PropertyAccessException {
	

		retryDelayExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

		retryAttemptsExpectation(mockPropertyAccessor, Expectations.returnValue("1"));

		timeoutExpectation(mockPropertyAccessor, Expectations.returnValue("1"));
		
		exceptionExpectation(mockPropertyAccessor, Expectations.throwException(new PropertyAccessException(
				"Failed to retrieve property.")));
	

		WebServiceProxyHelperProperties properties = new WebServiceProxyHelperProperties(
				mockPropertyAccessor);

		String sExceptionText = properties.getExceptionText();
		
		assertEquals("getExceptionText failed: ", "", sExceptionText);

	}

}
