/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocQueryDeferredRequest", portName = "AdapterDocQueryDeferredRequestPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredrequest.AdapterDocQueryDeferredRequestPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequest", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredRequestUnsecured/AdapterDocQueryDeferredRequest.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredRequestUnsecured {

    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (respondingGatewayCrossGatewayQueryRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            respondingGatewayCrossGatewayQueryRequest.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterDocQueryDeferredRequestOrchImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest(), respondingGatewayCrossGatewayQueryRequest.getAssertion());
    }

}
