/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindist.nhinc;

import gov.hhs.fha.nhinc.admindistribution.nhinc.NhincAdminDistOrchImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author dunnek
 */
@WebService(serviceName = "NhincAdminDistSecuredService", portName = "NhincAdminDistSecured_PortType", endpointInterface = "gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincadmindistribution", wsdlLocation = "WEB-INF/wsdl/NhincAdminDistSecured/NhincAdminDistSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincAdminDistSecured {
    @Resource
    private WebServiceContext context;
    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewaySendAlertMessageSecuredType body) {
        AssertionType assertion = extractAssertion(context);

        getNhincImpl().sendAlertMessage(body.getEDXLDistribution(),assertion, body.getNhinTargetSystem());
    }
    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }
    public NhincAdminDistOrchImpl getNhincImpl()
    {
        return new NhincAdminDistOrchImpl();
    }
}
