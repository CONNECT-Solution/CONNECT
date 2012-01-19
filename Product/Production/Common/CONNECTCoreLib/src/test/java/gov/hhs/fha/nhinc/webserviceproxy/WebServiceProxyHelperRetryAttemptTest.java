package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class WebServiceProxyHelperRetryAttemptTest extends AbstractWebServiceProxyHelpTest{

	
	/**
	 * Test the GetRetryAttempts method happy path.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetRetryAttemptsHappyPath() throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				ignoring(mockLog).debug(with(any(String.class)));
				oneOf(mockPropertyAccessor).getProperty(
						WebServiceProxyHelper.CONFIG_KEY_RETRYATTEMPTS);
				will(returnValue("5"));
			}
		});

		int iRetryAttempts = oHelper.getRetryAttempts();
		assertEquals("RetryAttempts failed.", 5, iRetryAttempts);

	}

	/**
	 * Test the GetRetryAttempts method with PropertyAccessException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetRetryAttemptsPropertyException() throws PropertyAccessException {
			context.checking(new Expectations() {

				{
					ignoring(mockLog).debug(with(any(String.class)));
					ignoring(mockLog).warn(with(any(String.class)));
					
					oneOf(mockPropertyAccessor).getProperty(
							WebServiceProxyHelper.CONFIG_KEY_RETRYATTEMPTS);
					will(throwException(new PropertyAccessException(
							"Failed to retrieve property.")));
				}
			});
		

			int iRetryAttempts = oHelper.getRetryAttempts();
			assertEquals("getRetryAttempts failed: ", 0, iRetryAttempts);
		
	}

	/**
	 * Test the GetRetryAttempts method with NumberFormatException.
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetRetryAttemptsNumberFormatException() throws PropertyAccessException {
			context.checking(new Expectations() {

				{
					ignoring(mockLog).debug(with(any(String.class)));
					ignoring(mockLog).warn(with(any(String.class)));
					
					oneOf(mockPropertyAccessor).getProperty(
							WebServiceProxyHelper.CONFIG_KEY_RETRYATTEMPTS);
					will(returnValue("NotANumber"));
				}
			});
			

			int iRetryAttempts = oHelper.getRetryAttempts();
			assertEquals("getRetryAttempts failed: ", 0, iRetryAttempts);
		
	}

}
