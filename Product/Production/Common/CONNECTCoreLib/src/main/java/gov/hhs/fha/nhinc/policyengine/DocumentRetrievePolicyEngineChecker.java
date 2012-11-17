package gov.hhs.fha.nhinc.policyengine;

import gov.hhs.fha.nhinc.common.eventcommon.DocRetrieveEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;

public interface DocumentRetrievePolicyEngineChecker {

    /**
     * This method will create the generic Policy Check Request Message from a document retrieve request
     * 
     * @param request Policy check request message for the document retrieve
     * @return A generic policy check request message that can be passed to the Policy Engine
     */
    public CheckPolicyRequestType checkPolicyDocRetrieve(DocRetrieveEventType request);

}