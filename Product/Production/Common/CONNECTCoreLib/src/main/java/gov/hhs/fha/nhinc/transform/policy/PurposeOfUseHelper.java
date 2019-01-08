/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

    public void appendPurposeOfUse(RequestType policyXacmlRequest, AssertionType assertion) {
        AssertionHelper assertHelp = new AssertionHelper();
        String purposeOfUse = assertHelp.extractPurpose(assertion);

        // there are two ways to do this, not sure which will be "correct", so adding to both for the moment - fix this
        // before releasing the code
        appendPurposeOfUseToAction(policyXacmlRequest, purposeOfUse);
        appendPurposeOfUseToSubject(policyXacmlRequest, purposeOfUse);
    }

    private void appendPurposeOfUseToAction(RequestType policyXacmlRequest, String purposeOfUse) {
        if (policyXacmlRequest != null && policyXacmlRequest.getAction() != null) {
            AttributeHelper attrHelper = new AttributeHelper();
            AttributeType purposeOfUseAttribute = attrHelper.attributeFactory(PurposeOfUseActionAttributeId,
                    Constants.DataTypeString, purposeOfUse);
            policyXacmlRequest.getAction().getAttribute().add(purposeOfUseAttribute);
        }
    }

    private void appendPurposeOfUseToSubject(RequestType policyXacmlRequest, String purposeOfUse) {
        if (policyXacmlRequest != null && policyXacmlRequest.getSubject() != null) {
            AttributeHelper attrHelper = new AttributeHelper();
            AttributeType purposeOfUseAttribute = attrHelper.attributeFactory(PurposeOfUseSubjectAttributeId,
                    Constants.DataTypeString, purposeOfUse);
            policyXacmlRequest.getSubject().get(0).getAttribute().add(purposeOfUseAttribute);
        }
    }
}
