/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.transform.policy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import oasis.names.tc.xacml._2_0.context.schema.os.AttributeType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;

/**
 *
 * @author rayj
 */
public class PurposeOfUseHelper {

    private static final String PurposeOfUseActionAttributeId = "urn:oasis:names:tc:xacml:2.0:action:purpose";
    private static final String PurposeOfUseSubjectAttributeId = "urn:gov:hhs:fha:nhinc:purpose-of-use";

    public void appendPurposeOfUse( RequestType policyXacmlRequest, AssertionType assertion) {
        AssertionHelper assertHelp = new AssertionHelper();
        String purposeOfUse = assertHelp.extractPurpose(assertion);

        //there are two ways to do this, not sure which will be "correct", so adding to both for the moment - fix this before releasing the code
        appendPurposeOfUseToAction(policyXacmlRequest, purposeOfUse);
        appendPurposeOfUseToSubject(policyXacmlRequest, purposeOfUse);
    }

    private void appendPurposeOfUseToAction( RequestType policyXacmlRequest, String purposeOfUse) {
        if ((policyXacmlRequest != null) &&   (policyXacmlRequest.getAction() != null)) {
            AttributeHelper attrHelper = new AttributeHelper();
            AttributeType purposeOfUseAttribute = attrHelper.attributeFactory(PurposeOfUseActionAttributeId, Constants.DataTypeString, purposeOfUse);
            policyXacmlRequest.getAction().getAttribute().add(purposeOfUseAttribute);
        }
    }

    private void appendPurposeOfUseToSubject( RequestType policyXacmlRequest, String purposeOfUse) {
        if ((policyXacmlRequest != null) &&   (policyXacmlRequest.getSubject() != null)) {
            AttributeHelper attrHelper = new AttributeHelper();
            AttributeType purposeOfUseAttribute = attrHelper.attributeFactory(PurposeOfUseSubjectAttributeId, Constants.DataTypeString, purposeOfUse);
            policyXacmlRequest.getSubject().get(0).getAttribute().add(purposeOfUseAttribute);
        }
    }
}
