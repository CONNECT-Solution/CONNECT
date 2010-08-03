package gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:36:16 PM
 */
public class AdapterDocRetrieveDeferredRespNoOpImpl implements AdapterDocRetrieveDeferredRespProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredRespNoOpImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body,
                                    AssertionType assertion) {
         DocRetrieveAcknowledgementType     response = new DocRetrieveAcknowledgementType();

         log.info("AdapterDocRetrieveDeferredRespNoOpImpl.sendToAdapter() - NO OP called");

         return response;
     }
}
