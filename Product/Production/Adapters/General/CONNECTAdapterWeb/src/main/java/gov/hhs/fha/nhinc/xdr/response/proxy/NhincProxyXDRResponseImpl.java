package gov.hhs.fha.nhinc.xdr.response.proxy;

import gov.hhs.fha.nhinc.async.AsyncMessageIdExtractor;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.passthru.xdr.async.response.proxy.PassthruXDRAsyncRespProxy;
import gov.hhs.fha.nhinc.passthru.xdr.async.response.proxy.PassthruXDRAsyncRespProxyObjectFactory;
import gov.hhs.fha.nhinc.service.WebServiceHelper;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.util.List;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author Neil Webb
 */
public class NhincProxyXDRResponseImpl
{
private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(NhincProxyXDRResponseImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBResponse(RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request, WebServiceContext context)
    {
        WebServiceHelper oHelper = new WebServiceHelper();
        XDRAcknowledgementType response = null;
        PassthruXDRAsyncRespProxyObjectFactory passthruXDRAsyncReqFactory = new PassthruXDRAsyncRespProxyObjectFactory();
        PassthruXDRAsyncRespProxy proxy = passthruXDRAsyncReqFactory.getPassthruXDRAsyncRespProxy();

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
