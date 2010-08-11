package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 1:34:33 PM
 */
public interface NhinDocRetrieveDeferredReqProxy {

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion);
}
