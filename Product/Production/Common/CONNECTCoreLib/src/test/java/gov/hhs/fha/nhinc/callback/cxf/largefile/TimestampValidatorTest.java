package gov.hhs.fha.nhinc.callback.cxf.largefile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.util.AbstractSuppressRootLoggerTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.cxf.message.Message;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSConfig;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.Timestamp;
import org.apache.ws.security.validate.Credential;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class TimestampValidatorTest extends AbstractSuppressRootLoggerTest{

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Before
	public void setUp() {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Test
	public void testValidate_NoProperties() throws Exception {
		Credential credential = new Credential();
		final String SECRET_KEY = "shhh...it's a secret";
		credential.setSecretKey(SECRET_KEY.getBytes());
		RequestData data = mock(RequestData.class);
		WSSConfig wssConfig = mock(WSSConfig.class);
		Message message = mock(Message.class);

		Timestamp credTimestamp = getTimestamp();
		credential.setTimestamp(credTimestamp);

		when(data.getWssConfig()).thenReturn(wssConfig);
		when(wssConfig.getTimeStampTTL()).thenReturn(300);
		when(wssConfig.getTimeStampFutureTTL()).thenReturn(60);

		when(data.getMsgContext()).thenReturn(message);
		when(message.get(TimestampInterceptor.INVOCATION_TIME_KEY)).thenReturn(
				getInvocationDate());

		TimestampValidator validator = new TimestampValidator();

		Credential resultCredential = validator.validate(credential, data);

		assertNotNull(resultCredential);
		assertEquals(resultCredential.getSecretKey(), credential.getSecretKey());
	}

	@Test(expected = WSSecurityException.class)
	public void validate_NullCredential() throws WSSecurityException {
		Credential credential = null;
		RequestData data = mock(RequestData.class);

		TimestampValidator validator = new TimestampValidator();
		validator.validate(credential, data);
	}

	@Test(expected = WSSecurityException.class)
	public void validate_NullData() throws WSSecurityException {
		Credential credential = mock(Credential.class);
		RequestData data = mock(RequestData.class);
		Timestamp timestamp = mock(Timestamp.class);

		when(credential.getTimestamp()).thenReturn(timestamp);

		TimestampValidator validator = new TimestampValidator();
		validator.validate(credential, data);
	}

	private Timestamp getTimestamp() throws WSSecurityException {
		// yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
		final String CREATE_TIME = "2013-01-01T01:00:00.000Z";
		final String EXPIRE_TIME = "2013-01-01T01:02:00.000Z";
		Element timestampElement = mock(Element.class);

		Element createdTimeElement = mock(Element.class);
		Element expiresTimeElement = mock(Element.class);
		Node node = null;
		Text dateTextNode = mock(Text.class);

		when(timestampElement.getFirstChild()).thenReturn(createdTimeElement);
		when(createdTimeElement.getNodeType()).thenReturn(Node.ELEMENT_NODE);
		when(createdTimeElement.getNextSibling()).thenReturn(
				expiresTimeElement, node);
		when(expiresTimeElement.getNodeType()).thenReturn(Node.ELEMENT_NODE);
		when(createdTimeElement.getLocalName()).thenReturn(
				WSConstants.CREATED_LN);
		when(createdTimeElement.getNamespaceURI()).thenReturn(
				WSConstants.WSU_NS);
		when(expiresTimeElement.getLocalName()).thenReturn(
				WSConstants.EXPIRES_LN);
		when(expiresTimeElement.getNamespaceURI()).thenReturn(
				WSConstants.WSU_NS);

		when(createdTimeElement.getFirstChild()).thenReturn(dateTextNode);
		when(expiresTimeElement.getFirstChild()).thenReturn(dateTextNode);

		when(dateTextNode.getData()).thenReturn(CREATE_TIME, EXPIRE_TIME);

		return new Timestamp(timestampElement);
	}

	private Date getInvocationDate() throws ParseException {
		return dateFormat.parse("2013-01-01T01:01:00.000Z");
	}

}
