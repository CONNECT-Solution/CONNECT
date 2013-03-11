package gov.hhs.fha.nhinc.callback.cxf;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.ws.security.WSPasswordCallback;
import org.junit.Test;

public class CXFPasswordCallbackHandlerTest {

	@Test
	public void testHandle() throws IOException, UnsupportedCallbackException {
		WSPasswordCallback wsPasswordCallback = new WSPasswordCallback(null, 0);
		Callback[] callbacks = new Callback[1];
		callbacks[0] = wsPasswordCallback;

		String password = "password";
		CXFPasswordCallbackHandler passwordHandler = new CXFPasswordCallbackHandler(
				password);
		passwordHandler.handle(callbacks);

		wsPasswordCallback = (WSPasswordCallback) callbacks[0];
		String resultPassword = wsPasswordCallback.getPassword();

		assertEquals(password, resultPassword);
	}

	@Test(expected = IOException.class)
	public void testHandle_IOException() throws IOException,
			UnsupportedCallbackException {
		WSPasswordCallback wsPasswordCallback = new WSPasswordCallback(null, 0);
		Callback[] callbacks = new Callback[1];
		callbacks[0] = wsPasswordCallback;

		String password = "";
		CXFPasswordCallbackHandler passwordHandler = new CXFPasswordCallbackHandler(
				password);
		passwordHandler.handle(callbacks);
	}

}
