/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindist.adapter;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import gov.hhs.fha.nhinc.admindistribution.adapter.AdapterAdminDistImpl;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
/**
 *
 * @author dunnek
 */
@WebService(serviceName = "Adapter_AdministrativeDistribution", portName = "Adapter_AdministrativeDistribution_PortType", endpointInterface = "gov.hhs.fha.nhinc.adapteradmindistribution.AdapterAdministrativeDistributionPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapteradmindistribution", wsdlLocation = "WEB-INF/wsdl/AdapterAdministrativeDistribution/AdapterAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterAdministrativeDistribution {
     @Resource
    private WebServiceContext context;

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewaySendAlertMessageType body) {
        new AdapterAdminDistImpl().sendAlertMessage(body.getEDXLDistribution(),body.getAssertion());
    }

}
