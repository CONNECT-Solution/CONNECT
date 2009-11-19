/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.adapterauthentication;

import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenID;
import com.iplanet.sso.SSOTokenManager;
import com.sun.identity.authentication.AuthContext;
import com.sun.identity.authentication.spi.AuthLoginException;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AdapterAuthenticationImpl {

    private static Log log = LogFactory.getLog(AdapterAuthenticationImpl.class);

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

        if (authenticateUserRequest != null) {
            try {
                String orgName = "";
                String userName = authenticateUserRequest.getUserName();
                String password = authenticateUserRequest.getPassword();
                SSOTokenManager manager = SSOTokenManager.getInstance();
                AuthContext lc = getAuthcontext(orgName, userName, password);
                if (lc.getStatus() == AuthContext.Status.SUCCESS) {
                    SSOToken token = lc.getSSOToken();
                    if (token != null) {
                        SSOTokenID tokenId = token.getTokenID();
                        if (tokenId != null) {
                            authResp.setIsAuthenticationAvailable(true);
                            authResp.setAuthenticationToken(tokenId.toString());
                        } else {
                            authResp = createInvalidUserResponse();
                            log.error("Empty SSOTokenID provided to AdapterAuthenticationImpl");
                        }
                    } else {
                        authResp = createInvalidUserResponse();
                        log.error("Null SSOToken provided to AdapterAuthenticationImpl");
                    }
                } else {
                    authResp = createInvalidUserResponse();
                        log.debug("Authentication Context failed in AdapterAuthenticationImpl");
                }
            } catch (AuthLoginException alex) {
                authResp = createInvalidUserResponse();
                log.error("AuthLoginException thrown from AdapterAuthenticationImpl: " + alex.getMessage());
            } catch (SSOException ssoex) {
                authResp = createInvalidUserResponse();
                log.error("SSOException thrown from AdapterAuthenticationImpl: " + ssoex.getMessage());
            } catch (Exception ex) {
                authResp = createInvalidUserResponse();
                log.error("Exception thrown from AdapterAuthenticationImpl: " + ex.getMessage());
            }
        } else {
            authResp = createInvalidUserResponse();
        }
        return authResp;
    }

    /**
     * A response indicating an unauthenticated user will be generated.  This
     * marks the authentication service as being available but sends an empty
     * authentication token.
     * @return a response indicating an unauthenticated user.
     */
    private AuthenticateUserResponseType createInvalidUserResponse() {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();
        authResp.setIsAuthenticationAvailable(true);
        authResp.setAuthenticationToken("");
        return authResp;
    }

    /**
     * Creates the authentication context which interfaces with the OpenSSO
     * authentication plug-in module.
     * @param orgName Not used, but required by OpenSSO
     * @param userName The user name to check for authentication
     * @param password The password of this user
     * @return The authentication context which interfaces with the OpenSSO
     * authentication plug-in module
     * @throws com.sun.identity.authentication.spi.AuthLoginException
     */
    private AuthContext getAuthcontext(String orgName, String userName, String password) throws AuthLoginException {
        AuthContext lc = new AuthContext(orgName);
        AuthContext.IndexType indexType = AuthContext.IndexType.MODULE_INSTANCE;
        String indexName = "DataStore";

        lc.login(indexType, indexName);

        Callback[] callback = lc.getRequirements();

        for (int i = 0; i < callback.length; i++) {
            if (callback[i] instanceof NameCallback) {
                NameCallback name = (NameCallback) callback[i];
                name.setName(userName);
            } else if (callback[i] instanceof PasswordCallback) {
                PasswordCallback pass = (PasswordCallback) callback[i];
                pass.setPassword(password.toCharArray());
            }
        }

        lc.submitRequirements(callback);
        return lc;
    }
}
