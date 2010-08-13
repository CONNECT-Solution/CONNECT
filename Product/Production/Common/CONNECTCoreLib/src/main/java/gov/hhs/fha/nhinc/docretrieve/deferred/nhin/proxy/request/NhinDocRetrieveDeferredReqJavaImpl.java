package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.request.NhinDocRetrieveDeferredReqImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:56:00 PM
 */
public class NhinDocRetrieveDeferredReqJavaImpl implements NhinDocRetrieveDeferredReqProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body,
                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType response = new DocRetrieveAcknowledgementType();
        NhinDocRetrieveDeferredReqImpl      nhinComponent = new NhinDocRetrieveDeferredReqImpl();

        response = nhinComponent.sendToRespondingGateway(body.getRetrieveDocumentSetRequest(), assertion);

        return response;
    }
}
