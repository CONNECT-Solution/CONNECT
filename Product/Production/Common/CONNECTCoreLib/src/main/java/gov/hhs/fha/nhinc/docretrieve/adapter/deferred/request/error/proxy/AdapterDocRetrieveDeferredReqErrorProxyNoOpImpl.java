package gov.hhs.fha.nhinc.docretrieve.adapter.deferred.request.error.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDocumentRetrieveDeferredRequestErrorSecuredType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 2:36:53 PM
 */
public class AdapterDocRetrieveDeferredReqErrorProxyNoOpImpl implements AdapterDocRetrieveDeferredReqErrorProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredReqErrorProxyNoOpImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(AdapterDocumentRetrieveDeferredRequestErrorSecuredType body,
                                                         AssertionType assertion) {
         DocRetrieveAcknowledgementType     response = new DocRetrieveAcknowledgementType();
         RegistryResponseType               resp = new RegistryResponseType();

         resp.setStatus("Success");
         response.setMessage(resp);

         log.info("AdapterDocRetrieveDeferredReqErrorNoOpImpl.sendToAdapter() - NO OP called");

         return response;
     }

}
