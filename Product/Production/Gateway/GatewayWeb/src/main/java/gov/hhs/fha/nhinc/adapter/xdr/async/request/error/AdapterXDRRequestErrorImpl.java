/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapter.xdr.async.request.error;

import gov.hhs.fha.nhinc.adapter.xdr.async.request.error.proxy.AdapterXDRRequestErrorProxy;
import gov.hhs.fha.nhinc.adapter.xdr.async.request.error.proxy.AdapterXDRRequestErrorProxyObjectFactory;
import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestErrorType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 *
 * @author JHOPPESC
 */
public class AdapterXDRRequestErrorImpl {
    private static final Log log = LogFactory.getLog(AdapterXDRRequestErrorImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(AdapterProvideAndRegisterDocumentSetRequestErrorType body, WebServiceContext context) {
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (body != null &&
                body.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            body.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        log.error("Received an error from the Gateway.  Message: " + body.getErrorMsg());

        AdapterXDRRequestErrorProxyObjectFactory adapterXdrAsyncReqErrorFactory = new AdapterXDRRequestErrorProxyObjectFactory();

        AdapterXDRRequestErrorProxy proxy = adapterXdrAsyncReqErrorFactory.getAdapterXDRRequestErrorProxy();

        ack = proxy.provideAndRegisterDocumentSetBRequestError(body);

        return ack;
    }

}
