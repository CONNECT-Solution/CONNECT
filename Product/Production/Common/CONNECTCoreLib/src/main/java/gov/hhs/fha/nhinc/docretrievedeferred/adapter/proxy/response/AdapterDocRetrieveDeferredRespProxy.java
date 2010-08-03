package gov.hhs.fha.nhinc.docretrievedeferred.adapter.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

/**
 * Created by
 * User: ralph
 * Date: Jul 28, 2010
 * Time: 12:41:47 PM
 */
public interface AdapterDocRetrieveDeferredRespProxy {

    public DocRetrieveAcknowledgementType sendToAdapter(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion);
}
