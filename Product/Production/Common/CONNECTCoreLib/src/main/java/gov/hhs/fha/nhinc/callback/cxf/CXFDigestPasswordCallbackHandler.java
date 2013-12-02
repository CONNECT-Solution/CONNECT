/**
 * 
 */
package gov.hhs.fha.nhinc.callback.cxf;

import gov.hhs.fha.nhinc.messaging.service.decorator.DigestAuthenticationServiceEndpointDecorator;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSPasswordCallback;
import org.springframework.util.Assert;

public class CXFDigestPasswordCallbackHandler implements CallbackHandler {

	private static final Logger LOG = Logger.getLogger(CXFDigestPasswordCallbackHandler.class);
	
    /**
     * Digest authentication callback handler retrieves the password from the hard coded (Java) system property    
     */
    public final void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    
    	Assert.notEmpty(callbacks, "The callback object must be provided");
    	Assert.isInstanceOf(WSPasswordCallback.class, callbacks[0], "The callback was not of type WSPasswordCallback");
    	
    	final WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
    	
    	final String webserviceUser = System.getProperty(DigestAuthenticationServiceEndpointDecorator.WEB_SERVICE_USER_SYSTEM_PROPERTY);
    	final String webservicePassword = System.getProperty(DigestAuthenticationServiceEndpointDecorator.WEB_SERVICE_PASSWORD_SYSTEM_PROPERTY);
		
    	if (StringUtils.isBlank(webserviceUser)){
			LOG.warn(String.format("The WebService user system property (%s) was not configured. This is likely to mean the web service call will fail"
					, DigestAuthenticationServiceEndpointDecorator.WEB_SERVICE_USER_SYSTEM_PROPERTY));
			return;
		}
    	
    	if (StringUtils.isBlank(webservicePassword)){
			LOG.warn(String.format("The WebService password system property (%s) was not configured. This is likely to mean the web service call will fail"
					, DigestAuthenticationServiceEndpointDecorator.WEB_SERVICE_PASSWORD_SYSTEM_PROPERTY));
			return;
		}
    	
    	final String userInWebServiceCall = pc.getIdentifier();
    	
    	if (!userInWebServiceCall.equals(webserviceUser)){
    		throw new SecurityException(String.format("The user in the web service call (%s) does not match the user configured with the system property (%s)", userInWebServiceCall, webserviceUser));
    	}
		
    	pc.setPassword(webservicePassword);
    }

}
