package gov.hhs.fha.nhinc.callback.cxf.largefile;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.util.AbstractSuppressRootLoggerTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityException;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class CONNECTTimestampTest extends AbstractSuppressRootLoggerTest{

	private static Element timestampElement = mock(Element.class);
	private static final String CREATE_TIME = "2013-01-01T01:00:05.000Z";
	private static final String EXPIRE_TIME = "2013-01-01T01:00:10.000Z";
	private static final String BEFORE_EXPIRE_TIME = "2013-01-01T01:00:05.000Z";
	private static final String AFTER_EXPIRE_TIME = "2013-01-01T01:00:15.000Z";

	private static final String INVOCATION_OLD_ERROR_TIME = "2013-01-01T01:00:06.000Z";
	private static final String INVOCATION_FUTURE_ERROR_TIME = "2013-01-01T01:00:02.000Z";
	private static final String INVOCATION_HAPPY_PATH = "2013-01-01T01:00:04.000Z";

	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

	@Before
	public void setUp() {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

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
		// yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
		when(dateTextNode.getData()).thenReturn(CREATE_TIME, EXPIRE_TIME);
	}

	@Test
	public void testIsExpired_False() throws WSSecurityException,
			ParseException {
		Date invocationDate = dateFormat.parse(BEFORE_EXPIRE_TIME);

		CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(
				timestampElement);
		boolean result = connectTimestamp.isExpired(invocationDate);

		assertFalse(result);
	}

	@Test
	public void testIsExpired_True() throws ParseException, WSSecurityException {
		Date invocationDate = dateFormat.parse(AFTER_EXPIRE_TIME);

		CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(
				timestampElement);
		boolean result = connectTimestamp.isExpired(invocationDate);

		assertTrue(result);
	}

	@Test
	public void testVerifyCreated_Pass() throws ParseException,
			WSSecurityException {
		Date invocationDate = dateFormat.parse(INVOCATION_HAPPY_PATH);

		CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(
				timestampElement);

		boolean result = connectTimestamp.verifyCreated(2, 2, invocationDate);

		assertTrue(result);
	}

	@Test
	public void testVerifyCreated_Fail_CreatedTimeInFuture()
			throws ParseException, WSSecurityException {
		Date invocationDate = dateFormat.parse(INVOCATION_FUTURE_ERROR_TIME);

		CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(
				timestampElement);

		boolean result = connectTimestamp.verifyCreated(2, 2, invocationDate);

		assertFalse(result);
	}

	@Test
	public void testVerifyCreated_Fail_CreatedTimeInPast()
			throws ParseException, WSSecurityException {
		Date invocationDate = dateFormat.parse(INVOCATION_OLD_ERROR_TIME);

		CONNECTTimestamp connectTimestamp = new CONNECTTimestamp(
				timestampElement);

		boolean result = connectTimestamp.verifyCreated(0, 2, invocationDate);

		assertFalse(result);
	}

}
