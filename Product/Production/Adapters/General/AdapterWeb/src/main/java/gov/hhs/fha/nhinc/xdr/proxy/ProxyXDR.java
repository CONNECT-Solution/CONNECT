/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;

import javax.jws.WebService;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "ProxyXDR_Service", portName = "ProxyXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdr.ProxyXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdr", wsdlLocation = "WEB-INF/wsdl/ProxyXDR/NhincProxyXDR.wsdl")
public class ProxyXDR {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType body) {
        //TODO implement this method
        return new ProxyXDRImpl().provideAndRegisterDocumentSetB(body);
    }

}
