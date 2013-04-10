package gov.hhs.fha.nhinc.policyengine.adapter.orchestrator.util;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResponseType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResultType;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author achidambaram
 *
 */
public class policyEngineUtil {

    private static final Logger LOG = Logger.getLogger(policyEngineUtil.class);

    public CheckPolicyResponseType checkkAssertionAttributeStatement(AssertionType assertion) {
        CheckPolicyResponseType oResponse = new CheckPolicyResponseType();
        if (assertion != null) {
            if (assertion.getUserInfo() != null) {
                PersonNameType personType = assertion.getUserInfo().getPersonName();
                if (personType != null) {
                    if (StringUtils.isBlank(personType.getFamilyName())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("FamilyName Check Failed");
                        return oResponse;
                    }
                    if (StringUtils.isBlank(personType.getSecondNameOrInitials())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("SecondNameOrInitials Check Failed");
                        return oResponse;
                    }
                    if (StringUtils.isBlank(personType.getGivenName())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("Given Name Check Failed");
                        return oResponse;
                    }
                    LOG.debug("Person Type Check Passed");
                } else {
                    oResponse = createResponseWithDENY(oResponse);
                    LOG.debug("Person Type Check Failed");
                    return oResponse;
                }
                if (assertion.getUserInfo().getOrg() != null) {
                    if (StringUtils.isBlank(assertion.getUserInfo().getOrg().getName())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("assertion.getUserInfo().getOrg().getName() Check Failed");
                        return oResponse;
                    }
                }
                if (assertion.getUserInfo().getRoleCoded() != null) {
                    if (StringUtils.isBlank(assertion.getUserInfo().getRoleCoded().getCode())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("assertion.getUserInfo().getRoleCoded().getCode() Check Failed");
                        return oResponse;
                    }
                    if (StringUtils.isBlank(assertion.getUserInfo().getRoleCoded().getCodeSystem())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("assertion.getUserInfo().getRoleCoded().getCodeSystem() Check Failed");
                        return oResponse;
                    }
                    if (StringUtils.isBlank(assertion.getUserInfo().getRoleCoded().getDisplayName())) {
                        oResponse = createResponseWithDENY(oResponse);
                        LOG.debug("assertion.getUserInfo().getRoleCoded().getDisplayName() Check Failed");
                        return oResponse;
                    }
                }
            } else {
                oResponse = createResponseWithDENY(oResponse);
                LOG.debug("User Info Check Failed");
                return oResponse;
            }
            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getCode())) {
                    oResponse = createResponseWithDENY(oResponse);
                    LOG.debug("getPurposeOfDisclosureCoded().getCode() Check Failed");

                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getCodeSystem())) {
                    oResponse = createResponseWithDENY(oResponse);
                    LOG.debug("getPurposeOfDisclosureCoded().getCodeSystem() Check Failed");

                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getCodeSystemName())) {
                    oResponse = createResponseWithDENY(oResponse);
                    LOG.debug("getPurposeOfDisclosureCoded().getCodeSystemName() Check Failed");

                    return oResponse;
                }
                if (StringUtils.isBlank(assertion.getPurposeOfDisclosureCoded().getDisplayName())) {
                    oResponse = createResponseWithDENY(oResponse);
                    LOG.debug("getPurposeOfDisclosureCoded().getDisplayName() Check Failed");

                    return oResponse;
                }
            } else {
                oResponse = createResponseWithDENY(oResponse);
                LOG.debug("PurposeOfDisclosureCoded Check Failed");
                return oResponse;
            }
            if (assertion.getHomeCommunity() != null) {
                if (StringUtils.isBlank(assertion.getHomeCommunity().getHomeCommunityId())) {
                    oResponse = createResponseWithDENY(oResponse);
                    LOG.debug("HomeCommunityId Check Failed");
                    return oResponse;
                }
            } else {
                oResponse = createResponseWithDENY(oResponse);
                LOG.debug("HomeCommunity Check Failed");
                return oResponse;
            }
        } else {
            oResponse = createResponseWithDENY(oResponse);
            LOG.debug("assertion check failed");
            return oResponse;
        }
        ResponseType Response = new ResponseType();
        ResultType oResult = new ResultType();
        oResult.setDecision(DecisionType.PERMIT);
        Response.getResult().add(oResult);
        oResponse.setResponse(Response);
        LOG.debug("Exiting out from CheckPolicyResponseType without any error");
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
