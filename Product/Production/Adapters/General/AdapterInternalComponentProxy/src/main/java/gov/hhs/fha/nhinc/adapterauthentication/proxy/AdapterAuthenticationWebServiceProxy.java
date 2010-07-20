package gov.hhs.fha.nhinc.adapterauthentication.proxy;

import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthentication;
import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationException;
import java.util.StringTokenizer;
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

        try {
            AdapterAuthenticationPortType authPort = getAdapterAuthenticationPort();
            int retryCount = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryAttempts();
            int retryDelay = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getRetryDelay();
            String exceptionText = gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().getExceptionText();
            javax.xml.ws.WebServiceException catchExp = null;
            if (retryCount > 0 && retryDelay > 0 && exceptionText != null && !exceptionText.equalsIgnoreCase("")) {
                int i = 1;
                while (i <= retryCount) {
                    try {
                        authResp = authPort.authenticateUser(authenticateUserRequest);
                        break;
                    } catch (javax.xml.ws.WebServiceException e) {
                        catchExp = e;
                        int flag = 0;
                        StringTokenizer st = new StringTokenizer(exceptionText, ",");
                        while (st.hasMoreTokens()) {
                            if (e.getMessage().contains(st.nextToken())) {
                                flag = 1;
                            }
                        }
                        if (flag == 1) {
                            log.warn("Exception calling ... web service: " + e.getMessage());
                            System.out.println("retrying the connection for attempt [ " + i + " ] after [ " + retryDelay + " ] seconds");
                            log.info("retrying attempt [ " + i + " ] the connection after [ " + retryDelay + " ] seconds");
                            i++;
                            try {
                                Thread.sleep(retryDelay);
                            } catch (InterruptedException iEx) {
                                log.error("Thread Got Interrupted while waiting on AdapterAuthenticationWebServiceProxy.authenticateUser call :" + iEx);
                            } catch (IllegalArgumentException iaEx) {
                                log.error("Thread Got Interrupted while waiting on AdapterAuthenticationWebServiceProxy.authenticateUser call :" + iaEx);
                            }
                            retryDelay = retryDelay + retryDelay; //This is a requirement from Customer
                        } else {
                            log.error("Unable to call AdapterAuthenticationWebServiceProxy.authenticateUser Webservice due to  : " + e);
                            throw e;
                        }
                    }
                }

                if (i > retryCount) {
                    log.error("Unable to call AdapterAuthenticationWebServiceProxy.authenticateUser Webservice due to  : " + catchExp);
                    throw catchExp;
                }

            } else {
                authResp = authPort.authenticateUser(authenticateUserRequest);
            }

        } catch (Exception ex) {
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
            throws AdapterAuthenticationException {
        AdapterAuthenticationPortType authPort = null;

        try {
            if (authService == null) {
                authService = new AdapterAuthentication();
            }

            authPort = authService.getAdapterAuthenticationPortSoap11();

            // Get the real endpoint URL for this service.
            String endpointURL = ConnectionManagerCache.getLocalEndpointURLByServiceName(ADAPTER_AUTH_SERVICE_NAME);

            if ((endpointURL == null) ||
                    (endpointURL.length() <= 0)) {
                endpointURL = ADAPTER_AUTH_DEFAULT_SERVICE_URL;
                String message = "Failed to retrieve the Endpoint URL for service: '" +
                        ADAPTER_AUTH_SERVICE_NAME + "'.  " +
                        "Setting this to: '" + endpointURL + "'";
                log.warn(message);
            }
            gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper.getInstance().initializePort((javax.xml.ws.BindingProvider) authPort, endpointURL);
        } catch (Exception ex) {
            String message = "Failed to retrieve a handle to the Adapter PIP web service.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new AdapterAuthenticationException(message, ex);
        }

        return authPort;
    }
}
