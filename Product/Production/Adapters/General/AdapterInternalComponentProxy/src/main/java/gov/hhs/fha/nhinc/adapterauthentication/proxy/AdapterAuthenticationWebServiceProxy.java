package gov.hhs.fha.nhinc.adapterauthentication.proxy;

import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthentication;
import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is the concrete implementation for the Web based call to the
 * AdapterAuthentication.
 */
public class AdapterAuthenticationWebServiceProxy {

    private static Log log = LogFactory.getLog(AdapterAuthenticationWebServiceProxy.class);
    private static AdapterAuthentication authService = null;
    private static String ADAPTER_AUTH_SERVICE_NAME = "adapterauthentication";
    private static String ADAPTER_AUTH_DEFAULT_SERVICE_URL = "http://localhost:8080/NhinConnect/AdapterAuthentication";


    /**
     * Given a request to authenticate a user, this service will determine if
     * this is an identifiable user within OpenSSO and if so will provide an
     * identifying token.
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is
     * implemented and if so the resulting token identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {

        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        try
        {
            AdapterAuthenticationPortType authPort = getAdapterAuthenticationPort();
            authResp = authPort.authenticateUser(authenticateUserRequest);
        }
        catch (Exception ex)
        {
            String message = "Error occurred calling AdapterAuthenticationWebServiceProxy.authenticateUser.  Error: " +
                                   ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }

        return authResp;
    }

    /**
     * Return a handle to the AdapterAuthentication web service.
     *
     * @return The handle to the Adapter Authentication port web service.
     */
    private AdapterAuthenticationPortType getAdapterAuthenticationPort()
        throws AdapterAuthenticationException
    {
        AdapterAuthenticationPortType authPort = null;

        try
        {
            if (authService == null)
            {
                authService = new AdapterAuthentication();
            }

            authPort = authService.getAdapterAuthenticationPortSoap11();

            // Get the real endpoint URL for this service.
            String endpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_AUTH_SERVICE_NAME);

            if ((endpointURL == null) ||
                (endpointURL.length() <= 0))
            {
                endpointURL = ADAPTER_AUTH_DEFAULT_SERVICE_URL;
                String message = "Failed to retrieve the Endpoint URL for service: '" +
                                       ADAPTER_AUTH_SERVICE_NAME + "'.  " +
                                       "Setting this to: '" + endpointURL + "'";
                log.warn(message);
            }
			gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) authPort, endpointURL);
        }
        catch (Exception ex)
        {
            String message = "Failed to retrieve a handle to the Adapter PIP web service.  Error: " +
                                   ex.getMessage();
            log.error(message, ex);
            throw new AdapterAuthenticationException(message, ex);
        }

        return authPort;
    }
}
