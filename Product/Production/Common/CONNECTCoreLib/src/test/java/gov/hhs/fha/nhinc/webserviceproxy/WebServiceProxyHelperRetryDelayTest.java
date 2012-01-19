package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class WebServiceProxyHelperRetryDelayTest extends
		AbstractWebServiceProxyHelpTest {

	/**
	 * Test the GetRetryDelay method happy path.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetRetryDelayHappyPath() throws PropertyAccessException {
			context.checking(new Expectations() {

				{
					ignoring(mockLog).debug(with(any(String.class)));
					oneOf(mockPropertyAccessor).getProperty(
							WebServiceProxyHelper.CONFIG_KEY_RETRYDELAY);
					will(returnValue("300"));
				}
			});
			

			int iRetryDelay = oHelper.getRetryDelay();
			assertEquals("RetryDelay failed.", 300, iRetryDelay);
	
	}

	/**
	 * Test the GetRetryDelay method with PropertyAccessException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetRetryDelayPropertyException() throws PropertyAccessException {
			context.checking(new Expectations() {

				{
					ignoring(mockLog).debug(with(any(String.class)));
					ignoring(mockLog).warn(with(any(String.class)));
					
					oneOf(mockPropertyAccessor).getProperty(
							WebServiceProxyHelper.CONFIG_KEY_RETRYDELAY);
					will(throwException(new PropertyAccessException(
							"Failed to retrieve property.")));
				}
			});
			

			int iRetryDelay = oHelper.getRetryDelay();
			assertEquals("getRetryDelay failed: ", 0, iRetryDelay);
		
	}

	/**
	 * Test the GetRetryDelay method with NumberFormatException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetRetryDelayNumberFormatException() throws PropertyAccessException {
			context.checking(new Expectations() {

				{
					ignoring(mockLog).debug(with(any(String.class)));
					ignoring(mockLog).warn(with(any(String.class)));
					
					oneOf(mockPropertyAccessor).getProperty(
							WebServiceProxyHelper.CONFIG_KEY_RETRYDELAY);
					will(returnValue("NotANumber"));
				}
			});
		
			int iRetryDelay = oHelper.getRetryDelay();
			assertEquals("getRetryDelay failed: ", 0, iRetryDelay);
		
	}
}
