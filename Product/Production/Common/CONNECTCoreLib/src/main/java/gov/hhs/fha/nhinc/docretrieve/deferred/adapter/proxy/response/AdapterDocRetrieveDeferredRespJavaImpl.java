package gov.hhs.fha.nhinc.docretrieve.deferred.adapter.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.response.AdapterDocRetrieveDeferredRespImpl;
import gov.hhs.fha.nhinc.docretrieve.deferred.adapter.response.AdapterDocRetrieveDeferredRespSecuredImpl;
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
public class AdapterDocRetrieveDeferredRespJavaImpl implements AdapterDocRetrieveDeferredRespProxy {
    private Log log = null;

     public AdapterDocRetrieveDeferredRespJavaImpl() {
         log = LogFactory.getLog(getClass());
     }

     public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body,
                                    AssertionType assertion) {
         DocRetrieveAcknowledgementType             response = new DocRetrieveAcknowledgementType();
         AdapterDocRetrieveDeferredRespSecuredImpl  adapter = new AdapterDocRetrieveDeferredRespSecuredImpl();

         response = adapter.respondingGatewayCrossGatewayRetrieve(body, assertion);

         return response;
     }
}
