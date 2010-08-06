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
@WebService(serviceName = "NhincAdminDistService", portName = "NhincAdminDist_PortType", endpointInterface = "gov.hhs.fha.nhinc.nhincadmindistribution.NhincAdminDistPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincadmindistribution", wsdlLocation = "WEB-INF/wsdl/NhincProxyAdminDist/NhincAdminDist.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincAdminDist {

    public void sendAlertMessage(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewaySendAlertMessageType body) {
            getNhincImpl().sendAlertMessage(body.getEDXLDistribution(),body.getAssertion(), body.getNhinTargetSystem());
    }
    public NhincAdminDistOrchImpl getNhincImpl()
    {
        return new NhincAdminDistOrchImpl();
    }
}
