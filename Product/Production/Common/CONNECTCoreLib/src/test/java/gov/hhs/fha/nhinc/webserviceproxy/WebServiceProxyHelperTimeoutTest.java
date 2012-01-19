package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class WebServiceProxyHelperTimeoutTest extends
		AbstractWebServiceProxyHelpTest {

	/**
	 * Test the GetTimeout method happy path.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetTimeoutHappyPath() throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				ignoring(mockLog).debug(with(any(String.class)));
				oneOf(mockPropertyAccessor).getProperty(
						WebServiceProxyHelper.CONFIG_KEY_TIMEOUT);
				will(returnValue("300"));
			}
		});

		int iTimeout = oHelper.getTimeout();
		assertEquals("Timeout failed.", 300, iTimeout);
	}

	/**
	 * Test the GetTimeout method with PropertyAccessException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetTimeoutPropertyException() throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				ignoring(mockLog).debug(with(any(String.class)));
				ignoring(mockLog).warn(with(any(String.class)));
				
				oneOf(mockPropertyAccessor).getProperty(
						WebServiceProxyHelper.CONFIG_KEY_TIMEOUT);
				will(throwException(new PropertyAccessException(
						"Failed to retrieve property.")));
			}
		});

		int iTimeout = oHelper.getTimeout();
		assertEquals("getTimeout failed: ", 0, iTimeout);

	}

	/**
	 * Test the GetTimeout method with NumberFormatException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetTimeoutNumberFormatException() throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				ignoring(mockLog).debug(with(any(String.class)));
				ignoring(mockLog).warn(with(any(String.class)));
				
				oneOf(mockPropertyAccessor).getProperty(
						WebServiceProxyHelper.CONFIG_KEY_TIMEOUT);
				will(returnValue("NotANumber"));
			}
		});

		int iTimeout = oHelper.getTimeout();
		assertEquals("getTimeout failed: ", 0, iTimeout);

	}

}
