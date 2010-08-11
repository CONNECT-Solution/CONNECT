/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.entity.deferred.response;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "EntityDocQueryDeferredResponseSecured", portName = "EntityDocQueryDeferredResponseSecuredPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocquerydeferredresponsesecured.EntityDocQueryDeferredResponseSecuredPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocquerydeferredresponsesecured", wsdlLocation = "WEB-INF/wsdl/EntityDocQueryDeferredResponseSecured/EntityDocQueryDeferredResponseSecured.wsdl")
@BindingType(value = "http://www.w3.org/2003/05/soap/bindings/HTTP/")
public class EntityDocQueryDeferredResponseSecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType crossGatewayQueryResponse(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryResponseSecuredType crossGatewayQueryResponse) {
        return new EntityDocQueryDeferredResponseSecuredImpl().crossGatewayQueryResponse(crossGatewayQueryResponse, context);
    }

}
