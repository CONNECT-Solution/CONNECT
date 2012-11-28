/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docretrieve.nhin;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import gov.hhs.fha.nhinc.policyengine.DocumentRetrievePolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;

/**
 * 
 * @author mweaver
 */
public class InboundDocRetrievePolicyTransformer_g0 implements PolicyTransformer {

    private  DocumentRetrievePolicyEngineChecker policyChecker;
    
    
    public InboundDocRetrievePolicyTransformer_g0() {
        policyChecker = new PolicyEngineChecker();
    }
    
    public InboundDocRetrievePolicyTransformer_g0(DocumentRetrievePolicyEngineChecker policyChecker) {
        this.policyChecker = policyChecker;
    }
    
    /**
     * @param message
     * @param direction - doesn't make sense. This is inbound.
     */
    public CheckPolicyRequestType transform(Orchestratable message, Direction direction) {
        CheckPolicyRequestType policyReq = null;
        if (message instanceof InboundDocRetrieveOrchestratable) {
            policyReq = transformInbound((InboundDocRetrieveOrchestratable) message);
        }
        return policyReq;
    }

    /**
     * To be removed!
     * @param direction
     * @param orchestratable
     * @return
     */
    public CheckPolicyRequestType transformOutbound(InboundDocRetrieveOrchestratable orchestratable) {
        CheckPolicyRequestType policyReq;
        DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
            policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);

        gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveMessageType();
        request.setAssertion(orchestratable.getAssertion());
        request.setRetrieveDocumentSetRequest(orchestratable.getRequest());
        policyCheckReq.setMessage(request);
        policyReq = policyChecker.checkPolicyDocRetrieve(policyCheckReq);
        return policyReq;
    }

    /**
     * @param direction
     * @param orchestratable
     * @return
     */
    public CheckPolicyRequestType transformInbound(InboundDocRetrieveOrchestratable orchestratable) {
        DocRetrieveMessageType request = new DocRetrieveMessageType();
        request.setAssertion(orchestratable.getAssertion());
        request.setRetrieveDocumentSetRequest(orchestratable.getRequest());
        return transformInbound(request);
    }

    /**
     * @param request
     * @return
     */
    public CheckPolicyRequestType transformInbound(DocRetrieveMessageType request) {
        DocRetrieveEventType policyCheckReq = new DocRetrieveEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_INBOUND_DIRECTION);
        policyCheckReq.setMessage(request);
        return policyChecker.checkPolicyDocRetrieve(policyCheckReq);
    }

}
