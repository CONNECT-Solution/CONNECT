package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.error;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.error.AdapterDocRetrieveDeferredReqErrorSecuredImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:36:53 PM
 */
public class AdapterDocRetrieveDeferredReqErrorJavaImpl implements AdapterDocRetrieveDeferredReqErrorProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredReqErrorJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(AdapterDocumentRetrieveDeferredRequestErrorSecuredType body,
                                                         AssertionType assertion) {
         DocRetrieveAcknowledgementType                 response = new DocRetrieveAcknowledgementType();
         AdapterDocRetrieveDeferredReqErrorSecuredImpl  errorAdapter = new AdapterDocRetrieveDeferredReqErrorSecuredImpl();

         response = errorAdapter.respondingGatewayCrossGatewayRetrieve(body, assertion);

         return response;
     }

}
