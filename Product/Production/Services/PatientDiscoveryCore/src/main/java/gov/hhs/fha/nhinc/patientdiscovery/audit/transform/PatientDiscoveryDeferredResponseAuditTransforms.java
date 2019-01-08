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
package gov.hhs.fha.nhinc.patientdiscovery.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PDMessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.parser.PRPAIN201306UV02Parser;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import javax.xml.bind.JAXBException;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author achidamb
 */
public class PatientDiscoveryDeferredResponseAuditTransforms extends
    AbstractPatientDiscoveryAuditTransforms<PRPAIN201306UV02, MCCIIN000002UV01> {

    private static final Logger LOG = LoggerFactory.getLogger(PatientDiscoveryDeferredResponseAuditTransforms.class);

    public PatientDiscoveryDeferredResponseAuditTransforms() {

    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForRequest(PRPAIN201306UV02 request,
        AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForResponse(request, auditMsg);
        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForResponse(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }

        return auditMsg;
    }

    @Override
    protected AuditMessageType getParticipantObjectIdentificationForResponse(PRPAIN201306UV02 request,
        MCCIIN000002UV01 response, AssertionType assertion, AuditMessageType auditMsg) {
        auditMsg = getPatientParticipantObjectIdentificationForResponse(request, auditMsg);

        try {
            auditMsg = getQueryParamsParticipantObjectIdentificationForResponse(request, auditMsg);
        } catch (JAXBException ex) {
            LOG.error("Error while creating ParticipantObjectIdentificationQueryByParameters segment : "
                + ex.getLocalizedMessage(), ex);
        }

        return auditMsg;
    }

    @Override
    protected AuditMessageType.ActiveParticipant getActiveParticipant(NhinTargetSystemType target, UserType oUserInfo) {
        return null;
    }

    @Override
    protected AuditMessageType.ActiveParticipant getActiveParticipantSource(NhinTargetSystemType target,
        String serviceName, boolean isRequesting, Properties webContextProperties, UserType oUserInfo) {

        String ipOrHost = isRequesting ? getPDDeferredRequestInitiatorAddress() : getLocalHostAddress();

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

        String url;
        String ipOrHost;

        AuditMessageType.ActiveParticipant participant = new AuditMessageType.ActiveParticipant();

        url = isRequesting ? getWebServiceUrlFromRemoteObject(getLocalHCIDNhinTarget(),
            NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME) : getWebServiceUrlFromRemoteObject(
                convertToNhinTarget(PRPAIN201306UV02Parser.getSenderHcid(getRequest())),
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        if (url != null) {
            try {
                participant.setUserID(url);
                ipOrHost = new URL(url).getHost();
                participant.setNetworkAccessPointID(ipOrHost);
                participant.setNetworkAccessPointTypeCode(getNetworkAccessPointTypeCode(ipOrHost));
            } catch (MalformedURLException ex) {
                LOG.error("Couldn't parse the given NetworkAccessPointID as a URL: " + ex.getLocalizedMessage(), ex);
                // The url is null or not a valid url; for now, set the user id to anonymous
                participant.setUserID(AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE);
                // TODO: For now, hardcode the value to localhost; need to find out if this needs to be set
                participant.setNetworkAccessPointTypeCode(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
                participant.setNetworkAccessPointID(AuditTransformsConstants.ACTIVE_PARTICIPANT_UNKNOWN_IP_ADDRESS);
            }
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

    protected String getPDDeferredRequestInitiatorAddress() {
        try {
            String hcid = PRPAIN201306UV02Parser.getReceiverHCID(getRequest());
            return new URL(getWebServiceUrlFromRemoteObject(PDMessageGeneratorUtils.getInstance().
                convertToNhinTargetSystemType(createNhinTargetCommunity(hcid)),
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME)).getHost();
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
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME);
        } catch (ExchangeManagerException ex) {
            LOG.error("Exception while Reading Url for the PatientDiscovery Deferred Response Service Endpoint: "
                + ex.getLocalizedMessage(), ex);
        }
        return AuditTransformsConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE;
    }

    private NhinTargetCommunityType createNhinTargetCommunity(String hcid) {
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType homecommunity = new HomeCommunityType();
        homecommunity.setHomeCommunityId(hcid);
        target.setHomeCommunity(homecommunity);
        return target;
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
        return PDMessageGeneratorUtils.getInstance().convertToNhinTargetSystemType(createNhinTargetCommunity(hcid));
    }

}
