package gov.hhs.fha.nhinc.xdr.request.entity;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.entity.xdr.async.request.proxy.EntityXDRAsyncReqProxy;
import gov.hhs.fha.nhinc.entity.xdr.async.request.proxy.EntityXDRAsyncReqProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
public class EntityXDRRequestImpl
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

        EntityXDRAsyncReqProxyObjectFactory entityXDRAsyncReqFactory = new EntityXDRAsyncReqProxyObjectFactory();

        EntityXDRAsyncReqProxy proxy = entityXDRAsyncReqFactory.getEntityXDRAsyncReqProxy();

        response = proxy.provideAndRegisterDocumentSetBAsyncRequest(request);

        return response;
    }
}
