/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request.error;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
public class AdapterXDRRequestErrorImpl {

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        XDRAcknowledgementType ack = new XDRAcknowledgementType();

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body != null &&
                body.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        return ack;
    }

}
