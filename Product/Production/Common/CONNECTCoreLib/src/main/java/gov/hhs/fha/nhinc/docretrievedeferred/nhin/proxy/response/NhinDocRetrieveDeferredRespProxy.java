package gov.hhs.fha.nhinc.docretrievedeferred.nhin.proxy.response;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredResponseType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

/**
 * Created by
 * User: ralph
 * Date: Jul 31, 2010
 * Time: 10:56:29 PM
 */
public interface NhinDocRetrieveDeferredRespProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredResponseType body, AssertionType assertion);
}
