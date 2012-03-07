/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrievePolicyTransformer_g0 implements PolicyTransformer {

    public CheckPolicyRequestType transform(Orchestratable message, Direction direction) {
        CheckPolicyRequestType policyReq = null;
        if (message instanceof InboundDocRetrieveOrchestratableImpl) {
            InboundDocRetrieveOrchestratableImpl NhinDROrchImpl_g0Message = (InboundDocRetrieveOrchestratableImpl) message;
            DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
            if (Direction.INBOUND == direction)
                policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
            else
                policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);

            gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType();
            request.setAssertion(NhinDROrchImpl_g0Message.getAssertion());
            request.setRetrieveDocumentSetRequest(NhinDROrchImpl_g0Message.getRequest());
            policyCheckReq.setMessage(request);
            PolicyEngineChecker policyChecker = new PolicyEngineChecker();
            policyReq = policyChecker.checkPolicyDocRetrieve(policyCheckReq);
        }
        return policyReq;
    }

}
