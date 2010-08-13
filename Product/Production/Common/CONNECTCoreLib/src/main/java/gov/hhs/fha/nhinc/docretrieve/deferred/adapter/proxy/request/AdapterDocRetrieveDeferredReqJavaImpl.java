package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.request.AdapterDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.request.AdapterDocRetrieveDeferredReqSecuredImpl;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.request.AdapterDocRetrieveDeferredReqSecuredImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:36:16 PM
 */
public class AdapterDocRetrieveDeferredReqJavaImpl implements AdapterDocRetrieveDeferredReqProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredReqJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body,
                                    AssertionType assertion) {
         DocRetrieveAcknowledgementType             response = new DocRetrieveAcknowledgementType();
         AdapterDocRetrieveDeferredReqSecuredImpl   adapter = new AdapterDocRetrieveDeferredReqSecuredImpl();

         response = adapter.respondingGatewayCrossGatewayRetrieve(body, assertion);

         return response;
     }
}
