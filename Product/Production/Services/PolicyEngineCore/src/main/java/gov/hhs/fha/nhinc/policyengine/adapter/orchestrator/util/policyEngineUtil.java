package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.util;

import org.apache.commons.lang.StringUtils;

import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
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
                if (StringUtils.isBlank(assertion.getPersonName().getFamilyName())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPersonName().getSecondNameOrInitials())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPersonName().getGivenName())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
            }
            else {
                oResponse = createResponseWithDENY(oResponse);
                return oResponse;
            }
            if (assertion.getUserInfo() != null) {
                if (assertion.getUserInfo().getOrg() != null) {
                    if (StringUtils.isBlank(assertion.getUserInfo().getOrg().getName())) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                }
                if (assertion.getUserInfo().getRoleCoded() != null) {
                    if (StringUtils.isBlank(assertion.getUserInfo().getRoleCoded().getCode())) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                    if (StringUtils.isBlank(assertion.getUserInfo().getRoleCoded().getCodeSystem())) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                    if (StringUtils.isBlank(assertion.getUserInfo().getRoleCoded().getDisplayName())) {
                        oResponse = createResponseWithDENY(oResponse);
                        return oResponse;
                    }
                }
            }
            else {
                oResponse = createResponseWithDENY(oResponse);
                return oResponse;
            }
            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getCode())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getCodeSystem())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getCodeSystemName())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getDisplayName())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
            }
            else {
                oResponse = createResponseWithDENY(oResponse);
                return oResponse;
            }
            if (assertion.getHomeCommunity() != null) {
                if (StringUtils.isBlank(assertion.getHomeCommunity().getHomeCommunityId())) {
                    oResponse = createResponseWithDENY(oResponse);
                    return oResponse;
                }
            }
            else {
                oResponse = createResponseWithDENY(oResponse);
                return oResponse;
            }
        }
        else {
            oResponse = createResponseWithDENY(oResponse);
            return oResponse;
        }
        ResponseType Response = new ResponseType();
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.PERMIT);
        Response.getResult().add(oResult);
        oResponse.setResponse(Response);
        return oResponse;
    }
    
    private CheckPolicyResponseType createResponseWithDENY(CheckPolicyResponseType response) {
        ResponseType Response = new ResponseType();
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.DENY);
        Response.getResult().add(oResult);
        response.setResponse(Response);
        return response;
    }
}
