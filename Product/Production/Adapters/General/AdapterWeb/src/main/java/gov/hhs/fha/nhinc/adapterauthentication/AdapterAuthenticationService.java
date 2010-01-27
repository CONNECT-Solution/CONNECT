/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterauthentication;

import javax.jws.WebService;
//import javax.xml.ws.WebServiceRef;


/**
 *
 * @author svalluripalli
 */
@WebService(serviceName = "AdapterAuthentication", portName = "AdapterAuthenticationPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.adapterauthentication.AdapterAuthenticationPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterauthentication", wsdlLocation = "WEB-INF/wsdl/AdapterAuthenticationService/AdapterAuthentication.wsdl")
public class AdapterAuthenticationService {
   // @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/AdapterAuthenticationService/AdapterAuthentication.wsdl")
    public gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserResponseType authenticateUser(gov.hhs.fha.nhinc.common.nhinccommonadapter.AuthenticateUserRequestType authenticateUserRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

}
