/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauthentication;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType;
import javax.ejb.Stateless;
import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@WebService(serviceName = "AdapterAuthentication", portName = "AdapterAuthenticationPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauthentication", wsdlLocation = "META-INF/wsdl/AdapterAuthenticationService/AdapterAuthentication.wsdl")
@Stateless
public class AdapterAuthenticationService implements AdapterAuthenticationPortType {

    private static Log log = LogFactory.getLog(AdapterAuthenticationService.class);

    /**
     * Given a request to authenticate a user, this service will determine if
     * an authentication service is configured for use (such as OpenSSO) and if
     * so will provide an identifying token.
     * @param authenticateUserRequest The request to authenticate the user
     * @return The response which indicates if an authentication service is
     * implemented and if so the resulting token identifier
     */
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType authenticateUser(gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType authenticateUserRequest) {
        AuthenticateUserResponseType authResp = new AuthenticateUserResponseType();

        AdapterAuthenticationImpl adapterAuthImpl = new AdapterAuthenticationImpl();

        try {
            authResp = adapterAuthImpl.authenticateUser(authenticateUserRequest);
        } catch (Exception ex) {
            String message = "Error occurred calling AdapterAuthenticationImpl.authenticateUser.  Error: " +
                    ex.getMessage();
            log.error(message, ex);
            throw new RuntimeException(message, ex);
        }
        return authResp;
    }

}
