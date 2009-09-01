/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;

/**
 *
 * @author rayj
 */
public class PurposeForUseHelper {

    private static final String PurposeForUseActionAttributeId = "urn:oasis:names:tc:xacml:2.0:action:purpose";
    private static final String PurposeForUseSubjectAttributeId = "urn:gov:hhs:fha:nhinc:purpose-for-use";

    public static void appendPurposeForUse(CheckPolicyRequestType policyRequest, AssertionType assertion) {
        String purposeForUse = AssertionHelper.extractPurpose(assertion);

        //there are two ways to do this, not sure which will be "correct", so adding to both for the moment - fix this before releasing the code
        appendPurposeForUseToAction(policyRequest, purposeForUse);
        appendPurposeForUseToSubject(policyRequest, purposeForUse);
    }

    private static void appendPurposeForUseToAction(CheckPolicyRequestType policyRequest, String purposeForUse) {
        if ((policyRequest != null) && (policyRequest.getRequest() != null) && (policyRequest.getRequest().getAction() != null)) {
            AttributeType purposeForUseAttribute = AttributeHelper.attributeFactory(PurposeForUseActionAttributeId, Constants.DataTypeString, purposeForUse);
            policyRequest.getRequest().getAction().getAttribute().add(purposeForUseAttribute);
        }
    }

    private static void appendPurposeForUseToSubject(CheckPolicyRequestType policyRequest, String purposeForUse) {
        if ((policyRequest != null) && (policyRequest.getRequest() != null) && (policyRequest.getRequest().getSubject().size() > 0)) {
            AttributeType purposeForUseAttribute = AttributeHelper.attributeFactory(PurposeForUseSubjectAttributeId, Constants.DataTypeString, purposeForUse);
            policyRequest.getRequest().getSubject().get(0).getAttribute().add(purposeForUseAttribute);
        }
    }
}
