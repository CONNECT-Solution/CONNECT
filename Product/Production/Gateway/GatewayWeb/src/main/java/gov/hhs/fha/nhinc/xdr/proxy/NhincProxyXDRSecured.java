/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.proxy;

import javax.jws.WebService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "ProxyXDRSecured_Service", portName = "ProxyXDRSecured_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdrsecured.ProxyXDRSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdrsecured", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRSecured/NhincProxyXDRSecured.wsdl")
public class NhincProxyXDRSecured {
    @Resource
    private WebServiceContext context;
    
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body) {
        //TODO implement this method
        return new NhincProxyXDRSecuredImpl().provideAndRegisterDocumentSetB(body, context);
    }

}
