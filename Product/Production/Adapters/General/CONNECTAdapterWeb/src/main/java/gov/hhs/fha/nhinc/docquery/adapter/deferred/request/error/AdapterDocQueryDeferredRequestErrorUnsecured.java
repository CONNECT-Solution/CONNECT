/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.docquery.adapter.component.deferred.request.AdapterComponentDocQueryDeferredRequestOrchImpl;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "AdapterDocQueryDeferredRequestError", portName = "AdapterDocQueryDeferredRequestErrorPortSoap", endpointInterface = "gov.hhs.fha.nhinc.adapterdocquerydeferredrequesterror.AdapterDocQueryDeferredRequestErrorPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequesterror", wsdlLocation = "WEB-INF/wsdl/AdapterDocQueryDeferredRequestErrorUnsecured/AdapterDocQueryDeferredRequestError.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class AdapterDocQueryDeferredRequestErrorUnsecured {
    @Resource
    private WebServiceContext context;

    public gov.hhs.healthit.nhin.DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorType respondingGatewayCrossGatewayQueryRequest) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (respondingGatewayCrossGatewayQueryRequest.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            respondingGatewayCrossGatewayQueryRequest.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterDocQueryDeferredRequestErrorOrchImpl().respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest.getAdhocQueryRequest(), respondingGatewayCrossGatewayQueryRequest.getAssertion(), respondingGatewayCrossGatewayQueryRequest.getErrorMsg());
    }

}
