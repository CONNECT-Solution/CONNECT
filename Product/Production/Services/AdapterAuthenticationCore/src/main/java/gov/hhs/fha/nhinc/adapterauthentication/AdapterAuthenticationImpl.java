/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.adapterauthentication;

import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenID;
import com.sun.identity.authentication.AuthContext;
import com.sun.identity.authentication.spi.AuthLoginException;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterAuthenticationImpl {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterAuthenticationImpl.class);

    /**
     * Given a request to authenticate a user, this service will determine if this is an identifiable user within
     * OpenSSO and if so will provide an identifying token.
     *
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is implemented and if so the resulting token
     * identifier
     */
    public AuthenticateUserResponseType authenticateUser(AuthenticateUserRequestType authenticateUserRequest) {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        if (authenticateUserRequest != null) {
            try {
                String orgName = "";
                String userName = authenticateUserRequest.getUserName();
                String password = authenticateUserRequest.getPassword();
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
                            LOG.error("Empty SSOTokenID provided to AdapterAuthenticationImpl");
                        }
                    } else {
                        authResp = createInvalidUserResponse();
                        LOG.error("Null SSOToken provided to AdapterAuthenticationImpl");
                    }
                } else {
                    authResp = createInvalidUserResponse();
                    LOG.debug("Authentication Context failed in AdapterAuthenticationImpl");
                }
            } catch (Exception ex) {
                authResp = createInvalidUserResponse();
                LOG.error("Exception thrown from AdapterAuthenticationImpl: {}", ex.getLocalizedMessage(), ex);
            }
        } else {
            authResp = createInvalidUserResponse();
        }
        return authResp;
    }

    /**
     * A response indicating an unauthenticated user will be generated. This marks the authentication service as being
     * available but sends an empty authentication token.
     *
     * @return a response indicating an unauthenticated user.
     */
    private AuthenticateUserResponseType createInvalidUserResponse() {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();
        authResp.setIsAuthenticationAvailable(true);
        authResp.setAuthenticationToken("");
        return authResp;
    }

    /**
     * Creates the authentication context which interfaces with the OpenSSO authentication plug-in module.
     *
     * @param orgName Not used, but required by OpenSSO
     * @param userName The user name to check for authentication
     * @param password The password of this user
     * @return The authentication context which interfaces with the OpenSSO authentication plug-in module
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
