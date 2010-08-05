package gov.hhs.fha.nhinc.xdr.request.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy.PassthruXDRAsyncReqProxy;
import gov.hhs.fha.nhinc.passthru.xdr.async.request.proxy.PassthruXDRAsyncReqProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRRequestImpl
{

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(RespondingGatewayProvideAndRegisterDocumentSetRequestType request, WebServiceContext context)
    {
        XDRAcknowledgementType response = null;

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setMessageId(msgIdExtractor.GetAsyncMessageId(context));
        }

        PassthruXDRAsyncReqProxyObjectFactory passthruXDRAsyncReqFactory = new PassthruXDRAsyncReqProxyObjectFactory();

        PassthruXDRAsyncReqProxy proxy = passthruXDRAsyncReqFactory.getPassthruXDRAsyncReqProxy();

        response = proxy.provideAndRegisterDocumentSetBAsyncRequest(request);

        return response;
    }
}
