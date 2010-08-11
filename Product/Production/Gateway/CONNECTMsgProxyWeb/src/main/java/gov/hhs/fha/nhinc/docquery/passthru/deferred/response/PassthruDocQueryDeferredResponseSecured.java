/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyDocQueryDeferredResponseSecured", portName = "NhincProxyDocQueryDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquerydeferredresponsesecured.NhincProxyDocQueryDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquerydeferredresponsesecured", wsdlLocation = "WEB-INF/wsdl/PassthruDocQueryDeferredResponseSecured/NhincProxyDocQueryDeferredResponseSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class PassthruDocQueryDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryResponseSecuredType body) {
        return new PassthruDocQueryDeferredResponseSecuredImpl().respondingGatewayCrossGatewayQuery(body, context);
    }

}
