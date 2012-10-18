/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;

/**
 *
 * @author dunnek
 */
public class AdminDistTransforms {
    private Log log = null;

    public AdminDistTransforms() {
        log = createLogger();
    }

    /**
     * Instantiating log4j logger.
     *
     * @return the instance of the logger
     */
    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public LogEventRequestType transformEntitySendAlertToAuditMsg(RespondingGatewaySendAlertMessageType message,
            AssertionType assertion, String direction, String _interface) {
        LogEventRequestType oReturnLogEventRequestType = null;
        EDXLDistribution body = null;

        if (message == null) {
            log.error("The Incoming Send Alert message was Null");
            return null;
        } else {
            body = message.getEDXLDistribution();
        }

        if (body == null || assertion == null) {
            log.error("The SendAlert did not have an EDXLDistribution or Assertion Object ");
            return null;
        }

        if ((message == null) || (assertion == null)) {
            log.error("The SendAlert did not have an EDXLDistribution or Assertion Object ");
            return null;
        } else {
            oReturnLogEventRequestType = transformEDXLDistributionRequestToAuditMsg(message.getEDXLDistribution(),
                    assertion, direction, _interface);
        }

        if (oReturnLogEventRequestType == null) {
            log.error("There was a problem translating the request into an audit log request object.");
            oReturnLogEventRequestType = null;
        } else {
            oReturnLogEventRequestType.setDirection(direction);
            oReturnLogEventRequestType.setInterface(_interface);
        }

        log.info("Exiting transformEntityPRPAIN201305RequestToAuditMsg() method.");

        return oReturnLogEventRequestType;

    }

    public LogEventRequestType transformEDXLDistributionRequestToAuditMsg(EDXLDistribution body,
            AssertionType assertion, NhinTargetSystemType target, String direction, String _interface) {
        {

            LogEventRequestType result = null;

            AuditMessageType auditMsg = null;

            log.debug("Entering transformPRPAIN201305RequestToAuditMsg() method.");

            auditMsg = new AuditMessageType();

            // check to see that the required fields are not null
            boolean bRequiredFieldsAreNull = areRequiredUserTypeFieldsNull(assertion);
            if (bRequiredFieldsAreNull) {
                // TODO add a unit test case...
                log.error("One or more of the required fields needed to transform to an audit message request were null.");
                return null;
            } // else continue
            if (target == null || target.getHomeCommunity() == null
                    || target.getHomeCommunity().getHomeCommunityId() == null) {
                log.error("One or more of the required fields needed to transform to an audit message request were null.");
            }

            // Extract UserInfo from request assertion
            UserType userInfo = assertion.getUserInfo();

            result = new LogEventRequestType();

            // Create EventIdentification
            CodedValueType eventID = new CodedValueType();
            eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,
                    AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST,
                    AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,
                    AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST);
            auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(
                    AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
                    AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

            // Create Active Participant Section
            AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                    userInfo, true);
            auditMsg.getActiveParticipant().add(participant);

            /* Assign AuditSourceIdentification */
            String communityId = "";
            String communityName = "";

            communityId = target.getHomeCommunity().getHomeCommunityId();
            communityName = target.getHomeCommunity().getName();

            /* Create the AuditSourceIdentifierType object */
            AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(
                    communityId, communityName);
            auditMsg.getAuditSourceIdentification().add(auditSource);

            result.setAuditMessage(auditMsg);
            result.setDirection(direction);
            result.setInterface(_interface);

            log.debug("Exiting transformEDXLDistributionRequestToAuditMsg() method.");

            return result;
        }
    }

    public LogEventRequestType transformEDXLDistributionRequestToAuditMsg(EDXLDistribution body,
            AssertionType assertion, String direction, String _interface) {
        LogEventRequestType result = null;

        AuditMessageType auditMsg = null;

        log.debug("Entering transformPRPAIN201305RequestToAuditMsg() method.");

        auditMsg = new AuditMessageType();

        // check to see that the required fields are not null
        boolean bRequiredFieldsAreNull = areRequiredUserTypeFieldsNull(assertion);
        if (bRequiredFieldsAreNull) {
            // TODO add a unit test case...
            log.error("One or more of the required fields needed to transform to an audit message request were null.");
            return null;
        } // else continue

        // Extract UserInfo from request assertion
        UserType userInfo = assertion.getUserInfo();

        result = new LogEventRequestType();

        // Create EventIdentification
        CodedValueType eventID = new CodedValueType();
        eventID = AuditDataTransformHelper.createEventId(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST,
                AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63,
                AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_ADMIN_DIST);
        auditMsg.setEventIdentification(AuditDataTransformHelper.createEventIdentification(
                AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE,
                AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS, eventID));

        // Create Active Participant Section
        AuditMessageType.ActiveParticipant participant = AuditDataTransformHelper.createActiveParticipantFromUser(
                userInfo, true);
        auditMsg.getActiveParticipant().add(participant);

        /* Assign AuditSourceIdentification */
        String communityId = "";
        String communityName = "";

        communityId = userInfo.getOrg().getHomeCommunityId();
        communityName = userInfo.getOrg().getName();

        /* Create the AuditSourceIdentifierType object */
        AuditSourceIdentificationType auditSource = AuditDataTransformHelper.createAuditSourceIdentification(
                communityId, communityName);
        auditMsg.getAuditSourceIdentification().add(auditSource);

        result.setAuditMessage(auditMsg);
        result.setDirection(direction);
        result.setInterface(_interface);

        log.debug("Exiting transformEDXLDistributionRequestToAuditMsg() method.");

        return result;

    }

    protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
        boolean bReturnVal = false;

        if ((oAssertion != null) && (oAssertion.getUserInfo() != null)) {
            if (oAssertion.getUserInfo().getUserName() != null) {
                log.debug("Incomming request.getAssertion.getUserInfo.getUserName: "
                        + oAssertion.getUserInfo().getUserName());
            } else {
                log.error("Incomming request.getAssertion.getUserInfo.getUserName was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getHomeCommunityId() != null) {
                log.debug("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId(): "
                        + oAssertion.getUserInfo().getOrg().getHomeCommunityId());
            } else {
                log.error("Incomming request.getAssertion.getUserInfo.getOrg().getHomeCommunityId() was null.");
                bReturnVal = true;
                return true;
            }

            if (oAssertion.getUserInfo().getOrg().getName() != null) {
                log.debug("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name: "
                        + oAssertion.getUserInfo().getOrg().getName());
            } else {
                log.error("Incomming request.getAssertion.getUserInfo.getOrg().getName() or Community Name was null.");
                bReturnVal = true;
                return true;
            }
        } else {
            log.error("The UserType object or request assertion object containing the assertion user info was null.");
            bReturnVal = true;
            return true;
        } // else continue

        return bReturnVal;
    }

}
