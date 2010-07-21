/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity;

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
@WebService(serviceName = "AdministrativeDistributionSecured_Service", portName = "AdministrativeDistributionSecured_PortType", endpointInterface = "gov.hhs.fha.nhinc.entityadmindistribution.AdministrativeDistributionSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entityadmindistribution", wsdlLocation = "WEB-INF/wsdl/EntityAdministrativeDistributionSecured/EntityAdminDistSecured.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityAdministrativeDistributionSecured {
    @Resource
    private WebServiceContext context;
    
    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType body) {
        //TODO implement this method

        AssertionType assertion = extractAssertion(context);

        //TODO implement this method
        new EntityAdminDistributionImpl().sendAlertMessage(body, assertion, body.getNhinTargetCommunities());
    }
    protected AssertionType extractAssertion(WebServiceContext context)
    {
        return SamlTokenExtractor.GetAssertion(context);
    }
}
