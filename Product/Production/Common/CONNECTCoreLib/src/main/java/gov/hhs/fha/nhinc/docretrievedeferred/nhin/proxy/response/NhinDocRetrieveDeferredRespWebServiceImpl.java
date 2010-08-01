package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:16:36 PM
 */
public class NhinDocRetrieveDeferredRespWebServiceImpl implements NhinDocRetrieveDeferredRespProxy {

    private Log log = null;

    public NhinDocRetrieveDeferredRespWebServiceImpl()
    {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion)
    {
        NhinDocRetrieveDeferredRespObjectFactory objectFactory = new NhinDocRetrieveDeferredRespObjectFactory();
        DocRetrieveAcknowledgementType           response = new DocRetrieveAcknowledgementType();

        logRequest(body, assertion);

        if(checkPolicy(body, assertion))
        {
            response = objectFactory.getDocumentDeferredResponseProxy().receiveFromAdapter(body, assertion);
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

    protected boolean checkPolicy(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion)
    {
        boolean result = false;

        // Call Sai's policy check class and return the result.

        return result;
    }

    protected void logRequest(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion)
    {

        // Call Sai's logging class using NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION.

        return;
    }

}
