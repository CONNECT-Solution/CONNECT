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
package gov.hhs.fha.nhinc.transform.audit;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.util.List;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author dunnek
 */
public class AdminDistTransforms {

    private static final Logger LOG = LoggerFactory.getLogger(AdminDistTransforms.class);

    public LogEventRequestType transformEDXLDistributionRequestToAuditMsg(EDXLDistribution body,
            AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {

        return getLogEventRequestType(body, assertion, direction, target, _interface);
    }

    public LogEventRequestType transformEDXLDistributionRequestToAuditMsg(EDXLDistribution body,
            AssertionType assertion, String direction, String _interface) {

        return getLogEventRequestType(body, assertion, direction, null, _interface);
    }

    protected LogEventRequestType getLogEventRequestType(EDXLDistribution body, AssertionType assertion,
            String direction, NhinTargetSystemType target, String _interface) {
        LOG.trace("Entering ADTransform-getLogEventRequestType() method.");

        LogEventRequestType result = new LogEventRequestType();
        AuditMessageType auditMsg = new AuditMessageType();

        boolean bRequiredFieldsAreNull = areRequiredUserTypeFieldsNull(assertion);
        if (bRequiredFieldsAreNull) {
            // TODO add a unit test case...
            LOG.error("One or more of the required fields needed to transform to an audit message request were null.");
            return null;
        } // else continue

        // Extract UserInfo from request assertion
        UserType userInfo = assertion.getUserInfo();
        // Create EventIdentification
        CodedValueType eventID;
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST);
        auditMsg.setEventIdentification(
                AuditDataTransformHelper.createEventIdentification(AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
                        AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper
                .createActiveParticipantFromUser(userInfo, true);
        auditMsg.getActiveParticipant().add(participant);

        /* Assign AuditSourceIdentification */
        String communityId = getAdminDistributionMessageCommunityID(assertion, direction, _interface, target);

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = AuditDataTransformHelper
                .createAuditSourceIdentification(communityId, communityId);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(_interface + " " + direction);
        result.setRemoteHCID(HomeCommunityMap.formatHomeCommunityId(communityId));
        result.setEventType(NhincConstants.NHIN_ADMIN_DIST_SERVICE_NAME);
        result.setEventID(auditMsg.getEventIdentification().getEventID().getDisplayName());
        result.setEventOutcomeIndicator(auditMsg.getEventIdentification().getEventOutcomeIndicator());
        result.setEventTimestamp(auditMsg.getEventIdentification().getEventDateTime());
        result.setAssertion(assertion);
        result.setRelatesTo(getRelatesTo(assertion));
        result.setRequestMessageId(assertion.getMessageId());
        LOG.trace("Exiting ADTransform-getLogEventRequestType() method.");

        return result;
    }

    protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {

        if (oAssertion != null && oAssertion.getUserInfo() != null) {
            if (oAssertion.getUserInfo().getUserName() != null) {
                LOG.debug("Incomming request.getAssertion.getUserInfo.getUserName: "
                        + oAssertion.getUserInfo().getUserName());
            } else {
                LOG.error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getHomeCommunityId() != null) {
                LOG.debug("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId(): "
                        + oAssertion.getUserInfo().getOrg().getHomeCommunityId());
            } else {
                LOG.error("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getName() != null) {
                LOG.debug("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name: "
                        + oAssertion.getUserInfo().getOrg().getName());
            } else {
                LOG.error("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
                return true;
            }
        } else {
            LOG.error("The UserType object or request assertion object containing the assertion user info was null.");
            return true;
        }

        return false;
    }

    public String getAdminDistributionMessageCommunityID(AssertionType assertion, String direction, String _interface,
            NhinTargetSystemType target) {

        String communityId = null;

        if (NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE.equalsIgnoreCase(_interface)
                || NhincConstants.AUDIT_LOG_ENTITY_INTERFACE.equalsIgnoreCase(_interface)) {
            communityId = getHomeCommunityFromMapping();
        } else if (NhincConstants.AUDIT_LOG_NHIN_INTERFACE.equalsIgnoreCase(_interface)) {
            if (NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION.equalsIgnoreCase(direction)) {
                communityId = HomeCommunityMap.getCommunityIdFromTargetSystem(target);
            } else {
                communityId = HomeCommunityMap.getHomeCommunityIdFromAssertion(assertion);
            }
        }

        return communityId;
    }

    /**
     * Needed for overriding the static HomeCommunityMapping call for unit test.
     *
     * @return
     */
    protected String getHomeCommunityFromMapping() {
        return HomeCommunityMap.getLocalHomeCommunityId();
    }

    private String getRelatesTo(AssertionType assertion) {
        return NullChecker.isNotNullish(assertion.getRelatesToList()) ? getRelatesToValue(assertion.getRelatesToList())
                : null;
    }

    private String getRelatesToValue(List<String> relatesTo) {
        return NullChecker.isNotNullish(relatesTo.get(0)) ? relatesTo.get(0) : null;
    }
}
