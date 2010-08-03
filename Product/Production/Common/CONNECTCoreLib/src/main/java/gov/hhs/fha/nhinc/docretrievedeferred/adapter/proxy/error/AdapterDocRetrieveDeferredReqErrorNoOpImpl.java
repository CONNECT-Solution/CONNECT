package gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.error;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.request.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:36:53 PM
 */
public class AdapterDocRetrieveDeferredReqErrorNoOpImpl implements AdapterDocRetrieveDeferredReqProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredReqErrorNoOpImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion) {
         DocRetrieveAcknowledgementType     response = new DocRetrieveAcknowledgementType();

         log.info("AdapterDocRetrieveDeferredReqErrorNoOpImpl.sendToAdapter() - NO OP called");

         return response;
     }

}
