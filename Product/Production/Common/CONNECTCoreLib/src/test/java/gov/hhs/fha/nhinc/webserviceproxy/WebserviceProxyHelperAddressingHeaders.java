package gov.hhs.fha.nhinc.webserviceproxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.async.AsyncHeaderCreator;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.junit.Test;

import com.sun.xml.ws.developer.WSBindingProvider;

public class WebserviceProxyHelperAddressingHeaders extends
		AbstractWebServiceProxyHelpTest {

	final WSBindingProvider mockPort = context.mock(WSBindingProvider.class);
	@SuppressWarnings("unchecked")
	final Map<String, Object> mockRequestContext = context.mock(Map.class);
	final HashMap<String, Object> oMap = new HashMap<String, Object>();
	final SamlTokenCreator mockTokenCreator = context
			.mock(SamlTokenCreator.class);
	final AsyncHeaderCreator mockAsyncHeaderCreator = context
			.mock(AsyncHeaderCreator.class);

	
	final Log mockLog = context.mock(Log.class);

	WebServiceProxyHelper oHelper;
	
	/**
	 * Tests the getWSAddressing method
	 * @throws PropertyAccessException 
	 */
	@Test
	public void testGetWSAddressingHeaders() throws PropertyAccessException {
		
		initializationExpectations();

		WebServiceProxyHelper oHelper = new WebServiceProxyHelper(mockLog, mockPropertyAccessor) {

			@Override
			protected Log createLogger() {
				return mockLog;
			}

			@Override
			protected AsyncHeaderCreator getAsyncHeaderCreator() {
				return new AsyncHeaderCreator() {

					@Override
					public List createOutboundHeaders(String url,
							String action, String messageId,
							List<String> relatesToIds) {

						List headers = new ArrayList();
						headers.add(url);
						headers.add(action);
						headers.add(messageId);
						headers.addAll(relatesToIds);
						return headers;
					}
				};
			}

			@Override
			protected String getMessageId(AssertionType assertion) {
				return "Test_Message_Id";
			}

			@Override
			protected List<String> getRelatesTo(AssertionType assertion) {
				List<String> allRelatesTo = new ArrayList();
				allRelatesTo.add("Test_Relates_1");
				allRelatesTo.add("Test_Relates_2");
				return allRelatesTo;
			}
		};

		AssertionType oAssertion = new AssertionType();
		List returnedHeaders = oHelper.getWSAddressingHeaders("Test_URL",
				"Test_ws_action", oAssertion);
		assertEquals("Number of created Headers is invalid.", 5,
				returnedHeaders.size());
		assertTrue("Test_URL header not found",
				returnedHeaders.contains("Test_URL"));
		assertTrue("Test_ws_action header not found",
				returnedHeaders.contains("Test_ws_action"));
		assertTrue("Test_Message_Id header not found",
				returnedHeaders.contains("Test_Message_Id"));
		assertTrue("Test_Relates_1 header not found",
				returnedHeaders.contains("Test_Relates_1"));
		assertTrue("Test_Relates_2 header not found",
				returnedHeaders.contains("Test_Relates_2"));
	}

	
}
