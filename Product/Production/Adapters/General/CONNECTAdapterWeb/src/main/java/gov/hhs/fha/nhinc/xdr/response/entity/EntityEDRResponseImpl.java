package gov.hhs.fha.nhinc.xdr.response.entity;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.entity.xdr.async.response.proxy.EntityXDRAsyncRespProxy;
import gov.hhs.fha.nhinc.entity.xdr.async.response.proxy.EntityXDRAsyncRespProxyObjectFactory;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
public class EntityEDRResponseImpl
{

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request, WebServiceContext context)
    {
        XDRAcknowledgementType response = null;

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        EntityXDRAsyncRespProxyObjectFactory entityXDRAsyncRespFactory = new EntityXDRAsyncRespProxyObjectFactory();

        EntityXDRAsyncRespProxy proxy = entityXDRAsyncRespFactory.getEntityXDRAsyncRespProxy();

        response = proxy.provideAndRegisterDocumentSetBAsyncResponse(request);

        return response;
    }

    

}
