package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 11:46:39 AM
 */
public class NhinDocRetrieveDeferredReqWebServiceImpl implements NhinDocRetrieveDeferredReqProxy {

    private Log log = null;

    public NhinDocRetrieveDeferredReqWebServiceImpl()
    {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion)
    {
        NhinDocRetrieveDeferredReqObjectFactory objectFactory;
        DocRetrieveAcknowledgementType          response = new DocRetrieveAcknowledgementType();

        logRequest(body, assertion);

        if(checkPolicy(body, assertion))
        {
            objectFactory = new NhinDocRetrieveDeferredReqObjectFactory();

            response = objectFactory.getDocumentDeferredRequestProxy().sendToRespondingGateway(body, assertion);
        }
        else {
            //
            // Call adapter error interface.
            //
        }

        //
        // We always end up returning a DocRetrieveAcknowledgementType because we are acknowledging receipt
        // of the request, not the successful processing of the message.
        //
        return response;
    }

    protected boolean checkPolicy(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion)
    {
        boolean result = false;

        // Call Sai's policy check class and return the result.

        return result;
    }

    protected void logRequest(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion)
    {

        // Call Sai's logging class using NhincConstants.AUDIT_LOG_INBOUND_DIRECTION.

        return;
    }

}
