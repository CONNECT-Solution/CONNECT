/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;

/**
 *
 * @author rayj
 */
public class PurposeForUseHelper {

    private static final String PurposeForUseActionAttributeId = "urn:oasis:names:tc:xacml:2.0:action:purpose";
    private static final String PurposeForUseSubjectAttributeId = "urn:gov:hhs:fha:nhinc:purpose-for-use";

    public static void appendPurposeForUse( RequestType policyXacmlRequest, AssertionType assertion) {
        String purposeForUse = AssertionHelper.extractPurpose(assertion);

        //there are two ways to do this, not sure which will be "correct", so adding to both for the moment - fix this before releasing the code
        appendPurposeForUseToAction(policyXacmlRequest, purposeForUse);
        appendPurposeForUseToSubject(policyXacmlRequest, purposeForUse);
    }

    private static void appendPurposeForUseToAction( RequestType policyXacmlRequest, String purposeForUse) {
        if ((policyXacmlRequest != null) &&   (policyXacmlRequest.getAction() != null)) {
            AttributeType purposeForUseAttribute = AttributeHelper.attributeFactory(PurposeForUseActionAttributeId, Constants.DataTypeString, purposeForUse);
            policyXacmlRequest.getAction().getAttribute().add(purposeForUseAttribute);
        }
    }

    private static void appendPurposeForUseToSubject( RequestType policyXacmlRequest, String purposeForUse) {
        if ((policyXacmlRequest != null) &&   (policyXacmlRequest.getAction() != null)) {
            AttributeType purposeForUseAttribute = AttributeHelper.attributeFactory(PurposeForUseActionAttributeId, Constants.DataTypeString, purposeForUse);
            policyXacmlRequest.getSubject().get(0).getAttribute().add(purposeForUseAttribute);
        }
    }
}
