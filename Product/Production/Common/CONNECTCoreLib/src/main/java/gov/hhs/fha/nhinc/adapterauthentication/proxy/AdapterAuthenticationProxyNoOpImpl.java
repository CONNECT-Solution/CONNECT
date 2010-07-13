package gov.hhs.fha.nhinc.adapterauthentication.proxy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;

/**
 * This is a "NoOp" implementation of the AdapterAuthenticationProxy interface.
 */
public class AdapterAuthenticationProxyNoOpImpl implements AdapterAuthenticationProxy {

    /**
     * NO-OP implementation of the authenticateUser operation returns a
     * response indicating that an authentication service is not available.
     * @return a response indicating no authentication service is set-up.
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();
        authResp.setIsAuthenticationAvailable(false);
        authResp.setAuthenticationToken("");
        return authResp;
    }
}
