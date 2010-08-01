package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 11:56:00 PM
 */
public class NhinDocRetrieveDeferredReqNoOpImpl implements NhinDocRetrieveDeferredReqProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion) {
        DocRetrieveAcknowledgementType ack = new DocRetrieveAcknowledgementType();
        RegistryResponseType resp = new RegistryResponseType();
        resp.setStatus("Success");
        ack.setMessage(resp);
        return ack;
    }
}
