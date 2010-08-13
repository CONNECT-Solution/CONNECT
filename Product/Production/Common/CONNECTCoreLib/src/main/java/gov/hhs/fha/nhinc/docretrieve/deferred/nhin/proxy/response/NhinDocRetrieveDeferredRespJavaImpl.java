package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.fha.nhinc.docretrieve.deferred.nhin.response.NhinDocRetrieveDeferredRespImpl;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import javax.xml.ws.WebServiceContext;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:25:14 PM
 */
public class NhinDocRetrieveDeferredRespJavaImpl implements NhinDocRetrieveDeferredRespProxy{

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body,
                                                                  AssertionType assertion) {
        DocRetrieveAcknowledgementType      response = new DocRetrieveAcknowledgementType();
        NhinDocRetrieveDeferredRespImpl     nhinComponent = new NhinDocRetrieveDeferredRespImpl();

        response =  nhinComponent.sendToRespondingGateway(body.getRetrieveDocumentSetResponse(), assertion);

        return response;
    }
}
