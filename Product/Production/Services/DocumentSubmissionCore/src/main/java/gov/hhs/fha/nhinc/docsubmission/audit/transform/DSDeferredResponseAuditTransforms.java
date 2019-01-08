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
package gov.hhs.fha.nhinc.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import gov.hhs.fha.nhinc.util.MessageGeneratorUtils;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class DSDeferredResponseAuditTransforms extends
    AbstractDocSubmissionAuditTransforms<RegistryResponseType, XDRAcknowledgementType> {

    private static final Logger LOG = LoggerFactory.getLogger(DSDeferredResponseAuditTransforms.class);

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(RegistryResponseType request,
        AssertionType assertion, AuditMessageType auditMsg) {
        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(RegistryResponseType request,
        XDRAcknowledgementType response, AssertionType assertion, AuditMessageType auditMsg) {
        return auditMsg;
    }

    @Override
    protected String getServiceEventIdCodeRequestor() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_RECIPIENT;
    }

    @Override
    protected String getServiceEventIdCodeResponder() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_SOURCE;
    }

    @Override
    protected String getServiceEventCodeSystem() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventDisplayRequestor() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_RECIPIENT;
    }

    @Override
    protected String getServiceEventDisplayResponder() {
        return DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_SOURCE;
    }

    @Override
    protected String getServiceEventTypeCode() {
        return DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE;
    }

    @Override
    protected String getServiceEventTypeCodeSystem() {
        return DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM;
    }

    @Override
    protected String getServiceEventTypeCodeDisplayName() {
        return DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME;
    }

    @Override
    protected String getServiceEventActionCodeRequestor() {
        return DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_RECIPIENT;
    }

    @Override
    protected String getServiceEventActionCodeResponder() {
        return DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_SOURCE;
    }

    @Override
    protected AuditMessageType.ActiveParticipant getActiveParticipant(NhinTargetSystemType target, UserType oUserInfo) {
        return null;
    }

    @Override
    protected AuditMessageType.ActiveParticipant getActiveParticipantSource(NhinTargetSystemType target,
        String serviceName, boolean isRequesting, Properties webContextProperties, UserType oUserInfo) {

        String ipOrHost = isRequesting ? getDSDeferredRequestInitiatorAddress(target) : getLocalHostAddress();

        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();
        participant.setUserID(isRequesting ? NhincConstants.WSA_REPLY_TO
            : getInboundReplyToFromHeader(webContextProperties));
        if (!isRequesting) {
            participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        }
        participant.setNetworkAccessPointID(ipOrHost);
        participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(ipOrHost));
        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE, null,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME));
        participant.setUserIsRequestor(Boolean.TRUE);

        String userName = getUserName(oUserInfo);
        if (userName != null) {
            participant.setUserName(userName);
        }
        return participant;

    }

    @Override
    protected AuditMessageType.ActiveParticipant getActiveParticipantDestination(NhinTargetSystemType target,
        boolean isRequesting, Properties webContextProperties, String serviceName) {

        String ipOrHost;

        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        String url = isRequesting ? getWebServiceUrlFromRemoteObject(getLocalHCIDNhinTarget(),
            NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME) : getWebServiceUrlFromRemoteObject(
                convertToNhinTarget(getMessageCommunityId(getAssertion(), target, isRequesting)),
                NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
        try {
            participant.setUserID(url);
            ipOrHost = new URL(url).getHost();
            participant.setNetworkAccessPointID(ipOrHost);
            participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(ipOrHost));
        } catch (MalformedURLException ex) {
            LOG.error("Couldn't parse the given NetworkAccessPointID as a URL: " + ex.getLocalizedMessage(), ex);
            participant.setUserID(AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE);
            // TODO: For now, hardcode the value to localhost; need to find out if this needs to be set
            participant.setNetworkAccessPointTypeCode(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
            participant.setNetworkAccessPointID(AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS);
        }
        if (isRequesting) {
            participant.setAlternativeUserID(ManagementFactory.getRuntimeMXBean().getName());
        }
        participant.setUserIsRequestor(Boolean.FALSE);
        participant.getRoleIDCode().add(AuditDataTransformHelper.createCodeValueType(
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, null,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME));

        return participant;
    }

    protected String getDSDeferredRequestInitiatorAddress(NhinTargetSystemType target) {
        try {
            return new URL(getWebServiceUrlFromRemoteObject(target,
                NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME)).getHost();
        } catch (MalformedURLException ex) {
            LOG.error("Exception while Reading Url for the PatientDiscovery Deferred Request Service Endpoint: "
                + ex.getLocalizedMessage(), ex);
        }
        return AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS;
    }

    @Override
    protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
        try {
            return getExchangeManager().getEndpointURLFromNhinTarget(target,
                NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            LOG.error("Exception while Reading Url for the PatientDiscovery Deferred Response Service Endpoint: "
                + ex.getLocalizedMessage(), ex);
        }
        return AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE;
    }

    private NhinTargetSystemType getLocalHCIDNhinTarget() {
        String hcid = null;
        try {
            hcid = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        } catch (PropertyAccessException ex) {
            LOG.error("Exception while reading local hcid from " + NhincConstants.GATEWAY_PROPERTY_FILE
                + ex.getLocalizedMessage(), ex);
        }
        return convertToNhinTarget(hcid);
    }

    private NhinTargetSystemType convertToNhinTarget(String hcid) {
        return MessageGeneratorUtils.getInstance().convertToNhinTargetSystemType(createNhinTargetCommunity(hcid));
    }

    private NhinTargetCommunityType createNhinTargetCommunity(String hcid) {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType homecommunity = new HomeCommunityType();
        homecommunity.setHomeCommunityId(hcid);
        target.setHomeCommunity(homecommunity);
        return target;
    }

}
