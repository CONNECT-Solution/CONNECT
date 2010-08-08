/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.response;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "AdapterDocQueryDeferredResponse", portName = "AdapterDocQueryDeferredResponsePortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredresponse.AdapterDocQueryDeferredResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredresponse", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredResponseUnsecured/AdapterDocQueryDeferredResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredResponseUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryResponseType respondingGatewayCrossGatewayQueryRequest) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (respondingGatewayCrossGatewayQueryRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            respondingGatewayCrossGatewayQueryRequest.getAssertion().setMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        return new AdapterDocQueryDeferredResponseOrchImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryResponse(), respondingGatewayCrossGatewayQueryRequest.getAssertion());
    }

}
