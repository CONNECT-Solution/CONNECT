package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:25:14 PM
 */
public class NhinDocRetrieveDeferredRespNoOpImpl implements NhinDocRetrieveDeferredRespProxy{

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion) {
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus("Success");
        ack.setMessage(resp);
        return ack;
    }
}
