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

import gov.hhs.fha.nhinc.common.eventcommon.XDSEventType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.util.format.PatientIdFormatUtil;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.xacml._2_0.context.schema.os.RequestType;
import oasis.names.tc.xacml._2_0.context.schema.os.ResourceType;
import oasis.names.tc.xacml._2_0.context.schema.os.SubjectType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XDSPolicyTransformHelper {

    private static final Logger LOG = LoggerFactory.getLogger(XDSPolicyTransformHelper.class);
    private static final String ACTION_IN_VALUE = "XDSIn";
    private static final String ACTION_OUT_VALUE = "XDSOut";
    private static final String PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID = Constants.AssigningAuthorityAttributeId;
    private static final String PATIENT_ID_ATTRIBUTE_ID = Constants.ResourceIdAttributeId;
    private static final String XDS_PATIENT_ID = "XDSSubmissionSet.patientId";

    /**
     * Transform method to create a CheckPolicyRequest object from a 201306 message
     *
     * @param request
     * @return CheckPolicyRequestType
     */

    public CheckPolicyRequestType transformXDSToCheckPolicy(XDSEventType event) {
        LOG.debug("Begin -- XDSPolicyTransformHelper.transformXDSToCheckPolicy()");
        CheckPolicyRequestType checkPolicyRequest = null;

        if (event == null) {
            LOG.debug("Request is null.");
            return checkPolicyRequest;
        } else {
            checkPolicyRequest = new CheckPolicyRequestType();
        }

        RequestType request = new RequestType();

        SubjectHelper subjHelp = new SubjectHelper();
        SubjectType subject = subjHelp.subjectFactory(event.getSendingHomeCommunity(),
            event.getMessage().getAssertion());
        LOG.debug("transformXDSToCheckPolicy - adding subject");
        request.getSubject().add(subject);

        String encodedPatientId = getPatientIdFromEvent(event);
        String assigningAuthorityId = PatientIdFormatUtil.parseCommunityId(encodedPatientId);
        String patId = PatientIdFormatUtil.parsePatientId(encodedPatientId);

        if (patId != null && assigningAuthorityId != null) {
            ResourceType resource = new ResourceType();
            AttributeHelper attrHelper = new AttributeHelper();
            resource.getAttribute().add(attrHelper.attributeFactory(PATIENT_ASSIGNING_AUTHORITY_ATTRIBUTE_ID,
                Constants.DataTypeString, assigningAuthorityId));

            LOG.debug("transformXDSToCheckPolicy: sStrippedPatientId = {}", patId);
            resource.getAttribute()
                .add(attrHelper.attributeFactory(PATIENT_ID_ATTRIBUTE_ID, Constants.DataTypeString, patId));

            request.getResource().add(resource);
        }

        LOG.debug("transformXDSToCheckPolicy - adding assertion data");
        AssertionHelper assertHelp = new AssertionHelper();
        assertHelp.appendAssertionDataToRequest(request, event.getMessage().getAssertion());

        if (NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION.equals(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTION_OUT_VALUE));
        } else if (NhincConstants.POLICYENGINE_INBOUND_DIRECTION.equals(event.getDirection())) {
            request.setAction(ActionHelper.actionFactory(ACTION_IN_VALUE));
        }

        checkPolicyRequest.setRequest(request);
        checkPolicyRequest.setAssertion(event.getMessage().getAssertion());
        LOG.debug("End -- XDSPolicyTransformHelper.transformXDSToCheckPolicy()");
        return checkPolicyRequest;
    }

    private static String getPatientIdFromEvent(XDSEventType event) {
        return getIdentifiersFromRequest(event.getMessage().getRegisterDocumentSetRequest());
    }

    private static String getIdentifiersFromRequest(RegisterDocumentSetRequestType request) {
        if (request == null) {
            LOG.error("Incoming RegisterDocumentSetRequestType was null");
            return null;
        }

        if (request.getSubmitObjectsRequest() == null) {
            LOG.error("Incoming RegisterDocumentSetRequestType metadata was null");
            return null;
        }

        LOG.debug("List of All Identifiable Registry Object(s) on Identifiers Form Request {}",
            request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable());
        RegistryObjectListType object = request.getSubmitObjectsRequest().getRegistryObjectList();

        for (int x = 0; x < object.getIdentifiable().size(); x++) {
            LOG.debug("Name of Identifiable Registry Object on Identifiers Form Request {}",
                object.getIdentifiable().get(x).getName());

            if (object.getIdentifiable().get(x).getDeclaredType().equals(RegistryPackageType.class)) {
                RegistryPackageType registryPackage = (RegistryPackageType) object.getIdentifiable().get(x).getValue();
                LOG.debug("Slot(s) in registry Package is {}", registryPackage.getSlot().size());
                return getPatientId(registryPackage);
            }
        }
        return null;
    }

    /**
     * @param registryPackage
     */
    private static String getPatientId(RegistryPackageType registryPackage) {
        for (int y = 0; y < registryPackage.getExternalIdentifier().size(); y++) {
            String test = registryPackage.getExternalIdentifier().get(y).getName().getLocalizedString().get(0)
                .getValue();
            if (XDS_PATIENT_ID.equals(test)) {
                return registryPackage.getExternalIdentifier().get(y).getValue();
            }

        }
        return null;
    }

}
