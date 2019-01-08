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
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.audit.transform.AuditTransforms;
import gov.hhs.fha.nhinc.audit.transform.AuditTransformsTest;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.docsubmission.audit.DocSubmissionAuditTransformsConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBException;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 *
 * @author achidamb
 */
public class DSDeferredResponseAuditTransformsTest extends
    AuditTransformsTest<RegistryResponseType, XDRAcknowledgementType> {

    private final String REQUEST_LOCAL_IP = "10.10.10.10";
    private final String REQUEST_REMOTE_IP = "16.14.13.12";
    private final String REQUEST_WS_REQUEST_URL = "http://" + REQUEST_LOCAL_IP + ":9090/AuditService";

    public DSDeferredResponseAuditTransformsTest() {
    }

    @Test
    public void testTransformRequestToAuditMsg() throws ConnectionManagerException, UnknownHostException, JAXBException {
        DSDeferredResponseAuditTransforms transforms = new DSDeferredResponseAuditTransforms() {

            @Override
            protected String getLocalHostAddress() {
                return REQUEST_LOCAL_IP;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return REQUEST_WS_REQUEST_URL;
            }

            @Override
            protected String getDSDeferredRequestInitiatorAddress(NhinTargetSystemType target) {
                return REQUEST_REMOTE_IP;
            }
        };

        RegistryResponseType request = new RegistryResponseType();

        AssertionType assertion = createAssertion();
        LogEventRequestType auditRequest = transforms.transformRequestToAuditMsg(request, assertion,
            createNhinTarget(), NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.TRUE, null, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
        testGetEventIdentificationType(auditRequest, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME, Boolean.TRUE);
        testCreateActiveParticipantFromUser(auditRequest, Boolean.TRUE, assertion);
        testGetActiveParticipantDestination(auditRequest, Boolean.TRUE, null, REQUEST_WS_REQUEST_URL, REQUEST_LOCAL_IP);
        testAuditSourceIdentification(auditRequest.getAuditMessage().getAuditSourceIdentification(), assertion);
        testGetActiveParticipantSource(auditRequest, Boolean.TRUE, null, REQUEST_REMOTE_IP);
        assertEquals("AuditMessage.Request direction mismatch", auditRequest.getDirection(),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        assertEquals("AuditMessage.Request ServiceName mismatch", auditRequest.getEventType(),
            NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
    }

    @Override
    protected void testCreateActiveParticipantFromUser(LogEventRequestType request, Boolean isRequesting,
        AssertionType assertion) {

        if (isRequesting) {
            AuditMessageType.ActiveParticipant userActiveParticipant = null;
            List<AuditMessageType.ActiveParticipant> activeParticipant
                = request.getAuditMessage().getActiveParticipant();
            for (AuditMessageType.ActiveParticipant item : activeParticipant) {
                // TODO: Where does this hard-coded value of "Code" come from?
                if (item.getRoleIDCode().get(0).getCode() != null && item.getRoleIDCode().get(0).getCode()
                    .equals("Code")) {

                    userActiveParticipant = item;
                    break;
                }
            }

            assertNull("userActiveParticipant is null", userActiveParticipant);
        }
    }

    @Test
    public void testTransformResponseToAuditMsg() throws ConnectionManagerException, UnknownHostException {

        Properties webContextProperties = new Properties();

        webContextProperties.setProperty(NhincConstants.WEB_SERVICE_REQUEST_URL, REQUEST_WS_REQUEST_URL);

        webContextProperties.setProperty(NhincConstants.REMOTE_HOST_ADDRESS, REQUEST_LOCAL_IP);

        webContextProperties.setProperty(NhincConstants.INBOUND_REPLY_TO, NhincConstants.WSA_REPLY_TO);
        DSDeferredResponseAuditTransforms transforms = new DSDeferredResponseAuditTransforms() {
            @Override
            protected String getLocalHostAddress() {
                return REQUEST_LOCAL_IP;
            }

            @Override
            protected String getRemoteHostAddress(Properties webContextProeprties) {
                return REQUEST_LOCAL_IP;
            }

            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return REQUEST_WS_REQUEST_URL;
            }
        };

        XDRAcknowledgementType response = new XDRAcknowledgementType();
        RegistryResponseType request = new RegistryResponseType();
        AssertionType assertion = createAssertion();
        LogEventRequestType auditResponse = transforms.transformResponseToAuditMsg(request, response, assertion, null,
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
            Boolean.FALSE, webContextProperties, NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);

        testGetEventIdentificationType(auditResponse, NhincConstants.NHINC_XDR_SERVICE_NAME, Boolean.FALSE);

        testGetActiveParticipantDestination(auditResponse, Boolean.FALSE, webContextProperties, REQUEST_LOCAL_IP);
        testAuditSourceIdentification(auditResponse.getAuditMessage().getAuditSourceIdentification(), assertion);
        testGetActiveParticipantSource(auditResponse, Boolean.FALSE, webContextProperties, REQUEST_REMOTE_IP);

        assertEquals(
            "AuditMessage.Response direction mismatch", auditResponse.getDirection(),
            NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
        assertEquals("AuditMessage.Response ServiceName mismatch", auditResponse.getEventType(),
            NhincConstants.NHINC_XDR_RESPONSE_SERVICE_NAME);
    }

    @Override
    protected void testGetEventIdentificationType(LogEventRequestType request, String serviceName,
        Boolean isRequesting) {

        EventIdentificationType eventIdentificationType = request.getAuditMessage().getEventIdentification();
        String eventActionCode;
        String eventIdCode;
        String eventIdDisplayName;

        if (isRequesting) {
            eventActionCode = DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_RECIPIENT;
            eventIdCode = DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_RECIPIENT;
            eventIdDisplayName = DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_RECIPIENT;
        } else {
            eventActionCode = DocSubmissionAuditTransformsConstants.EVENT_ACTION_CODE_SOURCE;
            eventIdCode = DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_DS_SOURCE;
            eventIdDisplayName = DocSubmissionAuditTransformsConstants.EVENT_ID_DISPLAY_SOURCE;
        }

        assertEquals("EventActionCode mismatch", eventActionCode, eventIdentificationType.getEventActionCode());
        assertEquals("EventId.Code", eventIdCode, eventIdentificationType.getEventID().getCode());
        assertEquals("EventId.DisplayName", eventIdDisplayName, eventIdentificationType.getEventID().getDisplayName());

        assertEquals("EventOutcomeIndicator mismatch", AuditTransformsConstants.EVENT_OUTCOME_INDICATOR_SUCCESS
            .toString(), eventIdentificationType.getEventOutcomeIndicator().toString());
        assertEquals("EventID.CodeSystemName mismatch", DocSubmissionAuditTransformsConstants.EVENT_ID_CODE_SYSTEM,
            eventIdentificationType.getEventID().getCodeSystemName());
        assertEquals("EventTypeCode.Code mismatch", DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE,
            eventIdentificationType.getEventTypeCode().get(0).getCode());
        assertEquals("EventTypeCode.CodeSystemName mismatch",
            DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_SYSTEM,
            eventIdentificationType.getEventTypeCode().get(0).getCodeSystemName());
        assertEquals("EventTypeCode.DisplayName mismatch",
            DocSubmissionAuditTransformsConstants.EVENT_TYPE_CODE_DISPLAY_NAME,
            eventIdentificationType.getEventTypeCode().get(0).getDisplayName());
    }

    @Override
    protected void checkAlternativeUserId(String alternativeUserID, Boolean isRequesting) {
        if (!isRequesting) {
            assertEquals("AlternativeUserId mismatch", ManagementFactory.getRuntimeMXBean().getName(),
                alternativeUserID);
        }
    }

    @Override
    protected AuditTransforms<RegistryResponseType, XDRAcknowledgementType> getAuditTransforms() {
        return new DSDeferredResponseAuditTransforms();
    }

}
