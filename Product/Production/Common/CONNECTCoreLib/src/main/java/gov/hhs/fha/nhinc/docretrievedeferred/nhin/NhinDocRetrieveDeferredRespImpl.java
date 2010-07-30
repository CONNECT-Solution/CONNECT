package gov.hhs.fha.nhinc.docretrievedeferred.nhin;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.NhinDocRetrieveDeferredReqObjectFactory;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.NhinDocRetrieveDeferredRespObjectFactory;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 11:50:51 AM
 */
public class NhinDocRetrieveDeferredRespImpl {

    private Log log = null;

    public NhinDocRetrieveDeferredRespImpl()
    {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion)
    {
        NhinDocRetrieveDeferredRespObjectFactory objectFactory;
        DocRetrieveAcknowledgementType           response = new DocRetrieveAcknowledgementType();
        logRequest(body, assertion);

        if(checkPolicy(body, assertion))
        {
            objectFactory = new NhinDocRetrieveDeferredRespObjectFactory();

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
