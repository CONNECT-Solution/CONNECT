package gov.hhs.fha.nhinc.xdr.response.entity;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.entity.xdr.async.response.proxy.EntityXDRAsyncRespProxy;
import gov.hhs.fha.nhinc.entity.xdr.async.response.proxy.EntityXDRAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
public class EntityEDRResponseImpl
{

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EntityEDRResponseImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request, WebServiceContext context)
    {
        WebServiceHelper oHelper = new WebServiceHelper();
        XDRAcknowledgementType response = null;
        EntityXDRAsyncRespProxyObjectFactory entityXDRAsyncRespFactory = new EntityXDRAsyncRespProxyObjectFactory();
        EntityXDRAsyncRespProxy proxy = entityXDRAsyncRespFactory.getEntityXDRAsyncRespProxy();

        try
        {
            if (request != null && proxy != null)
            {
                response = (XDRAcknowledgementType) oHelper.invokeDeferredResponseWebService(proxy, proxy.getClass(), "provideAndRegisterDocumentSetBAsyncResponse", request.getAssertion(), request, context);
            } else
            {
                log.error("Failed to call the web orchestration (" + proxy.getClass() + ".provideAndRegisterDocumentSetBAsyncResponse).  The input parameter is null.");
            }
        } catch (Exception e)
        {
            log.error("Failed to call the web orchestration (" + proxy.getClass() + ".provideAndRegisterDocumentSetBAsyncResponse).  An unexpected exception occurred.  " +
                    "Exception: " + e.getMessage(), e);
        }
        return response;
    }
}
