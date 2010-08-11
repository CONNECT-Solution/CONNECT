/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.error;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentQueryDeferredRequestErrorType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author jhoppesc
 */
public class AdapterDocQueryDeferredRequestErrorUnsecuredImpl {
    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdapterDocumentQueryDeferredRequestErrorType body, WebServiceContext context) {
        // Extract the relates to value from the WS-Addressing Header and place it in the Assertion Class
        if (body.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return new AdapterDocQueryDeferredRequestErrorOrchImpl().respondingGatewayCrossGatewayQuery(body.getAdhocQueryRequest(), body.getAssertion(), body.getErrorMsg());
    }

}
