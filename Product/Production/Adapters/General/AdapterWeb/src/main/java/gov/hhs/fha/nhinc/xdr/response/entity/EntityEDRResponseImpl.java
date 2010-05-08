package gov.hhs.fha.nhinc.xdr.response.entity;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import ihe.iti.xdr._2007.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.entity.xdr.async.response.proxy.EntityXDRAsyncRespProxy;
import gov.hhs.fha.nhinc.entity.xdr.async.response.proxy.EntityXDRAsyncRespProxyObjectFactory;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
public class EntityEDRResponseImpl
{

    public AcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request, WebServiceContext context)
    {
        AcknowledgementType response = null;

        // Extract the message id value from the WS-Addressing Header and place it in the Assertion Class
        if (request != null &&
                request.getAssertion() != null) {
            AsyncMessageIdExtractor msgIdExtractor = new AsyncMessageIdExtractor();
            request.getAssertion().setAsyncMessageId(msgIdExtractor.GetAsyncRelatesTo(context));
        }

        EntityXDRAsyncRespProxyObjectFactory entityXDRAsyncRespFactory = new EntityXDRAsyncRespProxyObjectFactory();

        EntityXDRAsyncRespProxy proxy = entityXDRAsyncRespFactory.getEntityXDRAsyncRespProxy();

        response = proxy.provideAndRegisterDocumentSetBAsyncResponse(request);

        return response;
    }

    

}
