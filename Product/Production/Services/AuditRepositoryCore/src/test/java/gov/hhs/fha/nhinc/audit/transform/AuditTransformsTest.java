/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import gov.hhs.fha.nhinc.audit.AuditTransformsConstants;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.CeType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author achidamb
 * @param <T> request Object Type
 * @param <K> response Object Type
 */
public abstract class AuditTransformsTest<T, K> {

    public AuditTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of createEventIdentification method, of class AuditTransforms.
     *
     * @param request
     * @param serviceName
     * @param isRequesting
     */
    protected void testGetEventIdentificationType(LogEventRequestType request, String serviceName,
        Boolean isRequesting) {

        EventIdentificationType eventIdentificationType = request.getAuditMessage().getEventIdentification();

        if (isRequesting) {
            assertEquals(getAuditTransforms().getServiceEventActionCodeRequestor(),
                eventIdentificationType.getEventActionCode());
            assertEquals(getAuditTransforms().getServiceEventIdCodeRequestor(),
                eventIdentificationType.getEventID().getCode());
            assertEquals(getAuditTransforms().getServiceEventDisplayRequestor(), eventIdentificationType.getEventID()
                .getDisplayName());
        } else {
            assertEquals(getAuditTransforms().getServiceEventActionCodeResponder(),
                eventIdentificationType.getEventActionCode());
            assertEquals(getAuditTransforms().getServiceEventIdCodeResponder(),
                eventIdentificationType.getEventID().getCode());
            assertEquals(getAuditTransforms().getServiceEventDisplayResponder(), eventIdentificationType.getEventID()
                .getDisplayName());
        }

        assertEquals("EventOutcomeIndicator mismatch", AuditTransformsConstants.EVENT_OUTCOME_INDICATOR_SUCCESS
            .toString(), eventIdentificationType.getEventOutcomeIndicator().toString());
        assertEquals("EventID.CodeSystemName mismatch", getAuditTransforms().getServiceEventCodeSystem(),
            eventIdentificationType.getEventID().getCodeSystemName());
        assertEquals("EventTypeCode.Code mismatch", getAuditTransforms().getServiceEventTypeCode(),
            eventIdentificationType.getEventTypeCode().get(0).getCode());
        assertEquals("EventTypeCode.CodeSystemName mismatch", getAuditTransforms().getServiceEventTypeCodeSystem(),
            eventIdentificationType.getEventTypeCode().get(0).getCodeSystemName());
        assertEquals("EventTypeCode.DisplayName mismatch", getAuditTransforms().getServiceEventTypeCodeDisplayName(),
            eventIdentificationType.getEventTypeCode().get(0).getDisplayName());
    }

    /**
     * Test of createActiveParticipantFromUser method, of class AuditTransforms.
     *
     * @param request
     * @param isRequesting
     * @param assertion
     */
    protected void testCreateActiveParticipantFromUser(LogEventRequestType request, Boolean isRequesting,
        AssertionType assertion) {

        if (isRequesting) {
            ActiveParticipant userActiveParticipant = null;
            List<ActiveParticipant> activeParticipant = request.getAuditMessage().getActiveParticipant();
            for (ActiveParticipant item : activeParticipant) {
                // TODO: Where does this hard-coded value of "Code" come from?
                if (item.getRoleIDCode().get(0).getCode() != null && item.getRoleIDCode().get(0).getCode()
                    .equals("Code")) {

                    userActiveParticipant = item;
                    break;
                }
            }

            assertNotNull("userActiveParticipant is null", userActiveParticipant);
            assertEquals("UserID mismatch", assertion.getUserInfo().getUserName(), userActiveParticipant.getUserID());
            assertEquals("UserName mismatch", assertion.getUserInfo().getPersonName().getGivenName() + " "
                + assertion.getUserInfo().getPersonName().getFamilyName(), userActiveParticipant.getUserName());
            assertEquals("RoleIDCode.Code mismatch", assertion.getUserInfo().getRoleCoded().getCode(),
                userActiveParticipant.getRoleIDCode().get(0).getCode());
            assertEquals("RoleIDCode.CodeSystemName mismatch", assertion.getUserInfo().getRoleCoded()
                .getCodeSystemName(), userActiveParticipant.getRoleIDCode().get(0).getCodeSystemName());
            assertEquals("RoleIDCode.DisplayName mismatch", assertion.getUserInfo().getRoleCoded().getDisplayName(),
                userActiveParticipant.getRoleIDCode().get(0).getDisplayName());
        }

        // TODO: Should there be an else case here?
    }

    /**
     * Test of getActiveParticipantSource method, of class AuditTransforms.
     *
     * @param request
     * @param webContextProperties
     * @param isRequesting
     * @param localIp
     * @throws java.net.UnknownHostException
     */
    protected void testGetActiveParticipantSource(LogEventRequestType request, Boolean isRequesting, String localIp,
        Properties webContextProperties) throws UnknownHostException {

        String ipOrHost;
        ActiveParticipant sourceActiveParticipant = null;
        List<ActiveParticipant> activeParticipant = request.getAuditMessage().getActiveParticipant();
        for (ActiveParticipant item : activeParticipant) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName()
                .equals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME)) {

                sourceActiveParticipant = item;
                break;
            }
        }

        assertNotNull("sourceActiveParticipant is null", sourceActiveParticipant);

        if (isRequesting) {
            assertEquals(NhincConstants.WSA_REPLY_TO, sourceActiveParticipant.getUserID());
            assertEquals(ManagementFactory.getRuntimeMXBean().getName(), sourceActiveParticipant
                .getAlternativeUserID());

            ipOrHost = localIp;
        } else {
            ipOrHost = webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS);
        }

        assertEquals(ipOrHost, sourceActiveParticipant.getNetworkAccessPointID());
        assertEquals(isRequesting, sourceActiveParticipant.isUserIsRequestor());
        assertEquals(getAuditTransforms().getNetworkAccessPointTypeCode(ipOrHost),
            sourceActiveParticipant.getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE,
            sourceActiveParticipant.getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME,
            sourceActiveParticipant.getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME,
            sourceActiveParticipant.getRoleIDCode().get(0).getDisplayName());
    }

    /**
     * Test of getActiveParticipantDestination method, of class AuditTransforms.
     *
     * @param request
     * @param isRequesting
     * @param webContextProperties
     * @param remoteObjectIP
     */
    protected void testGetActiveParticipantDestination(LogEventRequestType request, Boolean isRequesting,
        Properties webContextProperties, String remoteObjectIP) {

        ActiveParticipant destinationActiveParticipant = null;
        for (ActiveParticipant item : request.getAuditMessage().getActiveParticipant()) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName()
                .equals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME)) {

                destinationActiveParticipant = item;
            }
        }

        assertNotNull("destinationActiveParticipant is null", destinationActiveParticipant);

        if (isRequesting) {
            assertEquals(remoteObjectIP, destinationActiveParticipant.getUserID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL),
                destinationActiveParticipant.getUserID());
        }

        assertEquals(!isRequesting, destinationActiveParticipant.isUserIsRequestor());
        assertEquals(webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS),
            destinationActiveParticipant.getNetworkAccessPointID());
        assertEquals(AuditTransformsConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP, destinationActiveParticipant.
            getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, destinationActiveParticipant.
            getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME, destinationActiveParticipant.
            getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformsConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME,
            destinationActiveParticipant.getRoleIDCode().get(0).getDisplayName());
    }

    protected AssertionType createAssertion() {
        UserType userType = new UserType();
        userType.setOrg(createHomeCommunityType());
        userType.setPersonName(createPersonNameType());
        userType.setRoleCoded(createCeType());
        userType.setUserName("Wanderson");

        AssertionType assertion = new AssertionType();
        assertion.setUserInfo(userType);
        return assertion;
    }

    protected CeType createCeType() {
        CeType ceType = new CeType();
        ceType.setCode("Code");
        ceType.setCodeSystem("CodeSystem");
        ceType.setCodeSystemVersion("1.1");
        ceType.setDisplayName("DisplayName");
        return ceType;
    }

    protected HomeCommunityType createHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("1.1");
        homeCommunityType.setName("DOD");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    protected PersonNameType createPersonNameType() {
        PersonNameType personNameType = new PersonNameType();
        personNameType.setFamilyName("Tamney");
        personNameType.setFullName("Erica");
        personNameType.setGivenName("Jasmine");
        personNameType.setPrefix("Ms");
        return personNameType;
    }

    protected NhinTargetSystemType createNhinTarget() {
        NhinTargetSystemType targetSystem = new NhinTargetSystemType();
        targetSystem.setHomeCommunity(createTargetHomeCommunityType());
        return targetSystem;
    }

    protected HomeCommunityType createTargetHomeCommunityType() {
        HomeCommunityType homeCommunityType = new HomeCommunityType();
        homeCommunityType.setHomeCommunityId("2.2");
        homeCommunityType.setName("SSA");
        homeCommunityType.setDescription("This is DOD Gateway");
        return homeCommunityType;
    }

    protected abstract AuditTransforms<T, K> getAuditTransforms();
}
