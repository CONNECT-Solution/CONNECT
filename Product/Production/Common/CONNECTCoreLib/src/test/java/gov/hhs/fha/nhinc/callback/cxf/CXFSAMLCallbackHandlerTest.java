package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.callback.openSAML.CallbackProperties;
import gov.hhs.fha.nhinc.callback.openSAML.HOKSAMLAssertionBuilder;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;

import javax.security.auth.callback.Callback;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.cxf.message.Message;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.ws.security.saml.ext.SAMLCallback;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opensaml.common.SAMLVersion;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CXFSAMLCallbackHandlerTest {

	private static Element assertionElement;
	private static final String ASSERTION = "assertion";

	@BeforeClass
	public static void setUp() throws ParserConfigurationException {
		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(Level.ERROR);
		rootLogger.addAppender(new ConsoleAppender(new PatternLayout(
				"%-6r [%p] %c - %m%n")));
		createAssertionElement();

	}

	private static void createAssertionElement()
			throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		assertionElement = doc.createElement(ASSERTION);
		assertionElement.setTextContent(ASSERTION);
	}

	@Test
	public void testHandle() throws Exception {
		Callback[] callbackList = new Callback[1];
		SAMLCallback samlCallback = new SAMLCallback();
		callbackList[0] = samlCallback;
		final Message message = mock(Message.class);
		AssertionType assertionType = mock(AssertionType.class);
		HOKSAMLAssertionBuilder builder = mock(HOKSAMLAssertionBuilder.class);

		CXFSAMLCallbackHandler callbackHandler = new CXFSAMLCallbackHandler(
				builder) {
			@Override
			protected Message getCurrentMessage() {
				return message;
			}
		};

		when(message.get(anyString())).thenReturn(assertionType);
		when(message.get(NhincConstants.WS_SOAP_TARGET_HOME_COMMUNITY_ID))
				.thenReturn("1.1");
		when(message.get(NhincConstants.TARGET_API_LEVEL)).thenReturn("G0");
		when(message.get(NhincConstants.ACTION_PROP)).thenReturn("Soap Action");
		when(builder.build(any(CallbackProperties.class))).thenReturn(
				assertionElement);

		callbackHandler.handle(callbackList);

		assertEquals(samlCallback.getSamlVersion(), SAMLVersion.VERSION_20);
		assertEquals(samlCallback.getAssertionElement().getTextContent(),
				ASSERTION);
	}
}
