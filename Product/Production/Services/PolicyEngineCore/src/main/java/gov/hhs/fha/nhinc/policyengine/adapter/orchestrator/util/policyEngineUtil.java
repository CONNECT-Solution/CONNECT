package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.util;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;

/**
 * @author achidambaram
 * 
 */
public class policyEngineUtil {

    public CheckPolicyResponseType checkkAssertionAttributeStatement(AssertionType assertion) {

        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();
        if (assertion != null) {
            if (assertion.getPersonName() != null) {
                if (assertion.getPersonName().getFamilyName() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (assertion.getPersonName().getSecondNameOrInitials() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (assertion.getPersonName().getGivenName() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
            }
            if (assertion.getUserInfo() != null) {
                if (assertion.getUserInfo().getOrg() != null) {
                    if (assertion.getUserInfo().getOrg().getName() == null) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                }
                if (assertion.getUserInfo().getRoleCoded() != null) {
                    if (assertion.getUserInfo().getRoleCoded().getCode() == null) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                    if (assertion.getUserInfo().getRoleCoded().getCodeSystem() == null) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                    if (assertion.getUserInfo().getRoleCoded().getDisplayName() == null) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                }
            }
            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (assertion.getPurposeOfDisclosureCoded().getCode() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (assertion.getPurposeOfDisclosureCoded().getCodeSystem() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (assertion.getPurposeOfDisclosureCoded().getCodeSystemName() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (assertion.getPurposeOfDisclosureCoded().getDisplayName() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
            }
            if (assertion.getHomeCommunity() != null) {
                if (assertion.getHomeCommunity().getHomeCommunityId() == null) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
            }
        }
        return oResponse;
    }
    
    private CheckPolicyResponseType createResponseWithDENY(CheckPolicyResponseType response) {
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.DENY);
        response.getResponse().getResult().add(oResult);
        return response;
    }
}
