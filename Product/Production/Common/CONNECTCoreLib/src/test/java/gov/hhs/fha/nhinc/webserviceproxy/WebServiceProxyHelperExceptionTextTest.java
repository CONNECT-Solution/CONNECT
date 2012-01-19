package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JMock;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class WebServiceProxyHelperExceptionTextTest extends AbstractWebServiceProxyHelpTest {
	
	/**
	 * Test the GetExceptionText method happy path.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetExceptionTextHappyPath() throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				ignoring(mockLog).debug(with(any(String.class)));

				oneOf(mockPropertyAccessor).getProperty(
						WebServiceProxyHelper.CONFIG_KEY_EXCEPTION);
				will(returnValue("PropertyAccessException"));
			}
		});

		String sExceptionText = oHelper.getExceptionText();
		assertEquals("getExceptionText failed.", "PropertyAccessException",
				sExceptionText);

	}

	/**
	 * Test the GetExceptionText method with PropertyAccessException.
	 * 
	 * @throws PropertyAccessException
	 */
	@Test
	public void testGetExceptionTextPropertyException()
			throws PropertyAccessException {
		context.checking(new Expectations() {

			{
				ignoring(mockLog).debug(with(any(String.class)));
				ignoring(mockLog).warn(with(any(String.class)));

				oneOf(mockPropertyAccessor).getProperty(
						WebServiceProxyHelper.CONFIG_KEY_EXCEPTION);
				will(throwException(new PropertyAccessException(
						"Failed to retrieve property.")));
			}
		});

		String sExceptionText = oHelper.getExceptionText();
		assertEquals("getExceptionText failed: ", "", sExceptionText);

	}

}
