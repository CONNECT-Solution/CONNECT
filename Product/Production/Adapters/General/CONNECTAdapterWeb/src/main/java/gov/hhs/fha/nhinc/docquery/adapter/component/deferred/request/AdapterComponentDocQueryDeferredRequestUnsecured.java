/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterComponentDocQueryDeferredRequest", portName = "AdapterComponentDocQueryDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adaptercomponentdocquerydeferredrequest.AdapterComponentDocQueryDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adaptercomponentdocquerydeferredrequest", wsdlLocation = "WEB-INF/wsdl/AdapterComponentDocQueryDeferredRequestUnsecured/AdapterComponentDocQueryDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterComponentDocQueryDeferredRequestUnsecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (respondingGatewayCrossGatewayQueryRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            respondingGatewayCrossGatewayQueryRequest.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterComponentDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest(), respondingGatewayCrossGatewayQueryRequest.getAssertion(), null);
    }

}
