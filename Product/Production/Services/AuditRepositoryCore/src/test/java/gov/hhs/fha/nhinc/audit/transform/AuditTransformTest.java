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
import gov.hhs.fha.nhinc.audit.AuditTransformConstants;
import gov.hhs.fha.nhinc.audit.AuditTransformDataBuilder;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 *
 * @author achidamb
 */
public abstract class AuditTransformTest {

    public AuditTransformTest() {
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
     * Test of createEventIdentification method, of class AuditTransform.
     *
     * @param request
     * @param serviceName
     * @param isRequesting
     * @param builder
     */
    protected void testGetEventIdentificationType(LogEventRequestType request, String serviceName, Boolean isRequesting, 
        AuditTransformDataBuilder builder) {
        EventIdentificationType eventIdentificationType = request.getAuditMessage().getEventIdentification();
        if (isRequesting) {
            assertEquals(builder.getServiceEventActionCodeRequestor(), eventIdentificationType.getEventActionCode());
        } else {
            assertEquals(builder.getServiceEventActionCodeResponder(), eventIdentificationType.getEventActionCode());
        }
        assertEquals(AuditTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS.toString(), eventIdentificationType.
            getEventOutcomeIndicator().toString());
        assertEquals(builder.getServiceEvenIdCode(), eventIdentificationType.getEventID().getCode());
        assertEquals(builder.getServiceEventCodeSystem(), eventIdentificationType.getEventID().getCodeSystemName());
        if (isRequesting) {
            assertEquals(builder.getServiceEventDisplayRequestor(), eventIdentificationType.getEventID().
                getDisplayName());
        } else {
            assertEquals(builder.getServiceEventDisplayResponder(), eventIdentificationType.getEventID().
                getDisplayName());
        }

        assertEquals(builder.getServiceEventTypeCode(), eventIdentificationType.getEventTypeCode().get(0).getCode());
        assertEquals(builder.getServiceEventTypeCodeSystem(),eventIdentificationType.getEventTypeCode().get(0).
            getCodeSystemName());
        assertEquals(builder.getServiceEventTypeCodeDisplayName(), eventIdentificationType.getEventTypeCode().get(0).
            getDisplayName());
    }

    /**
     * Test of createActiveParticipantFromUser method, of class AuditTransform.
     *
     * @param request
     * @param isRequesting
     * @param assertion
     */
    protected void testCreateActiveParticipantFromUser(LogEventRequestType request, Boolean isRequesting, AssertionType assertion) {
        if (isRequesting) {
            ActiveParticipant userActiveParticipant = null;
            List<ActiveParticipant> activeParticipant = request.getAuditMessage().getActiveParticipant();
            for (ActiveParticipant item : activeParticipant) {
                if (item.getRoleIDCode().get(0).getCode() != null && item.getRoleIDCode().get(0).getCode().
                    equals("Code")) {
                    userActiveParticipant = item;
                }
            }
            assertEquals(assertion.getUserInfo().getUserName(), userActiveParticipant.getUserID());
            assertEquals(assertion.getUserInfo().getPersonName().getGivenName() + " " + 
                assertion.getUserInfo().getPersonName().getFamilyName(), userActiveParticipant.getUserName());
            assertEquals(assertion.getUserInfo().getRoleCoded().getCode(), userActiveParticipant.
                getRoleIDCode().get(0).getCode());
            assertEquals(assertion.getUserInfo().getRoleCoded().getCodeSystemName(), userActiveParticipant.
                getRoleIDCode().get(0).getCodeSystemName());
            assertEquals(assertion.getUserInfo().getRoleCoded().getDisplayName(), userActiveParticipant.
                getRoleIDCode().get(0).getDisplayName());
        }
    }

    /**
     * Test of getActiveParticipantSource method, of class AuditTransform.
     *
     * @param request
     * @param webContextProperties
     * @param isRequesting
     * @param localIP
     * @throws java.net.UnknownHostException
     */
    protected void testGetActiveParticipantSource(LogEventRequestType request, Boolean isRequesting, String localIP, 
        Properties webContextProperties) throws UnknownHostException {
        ActiveParticipant sourceActiveParticipant = null;
        List<ActiveParticipant> activeParticipant = request.getAuditMessage().getActiveParticipant();
        for (ActiveParticipant item : activeParticipant) {
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).getDisplayName().
                equals(AuditTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME)) {
                sourceActiveParticipant = item;
            }
        }
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_USER_ID_SOURCE, sourceActiveParticipant.getUserID());
        if (isRequesting) {
            assertEquals(ManagementFactory.getRuntimeMXBean().getName(), sourceActiveParticipant.getAlternativeUserID());
        }
        assertEquals(isRequesting, sourceActiveParticipant.isUserIsRequestor());
        if (isRequesting) {
            assertEquals(localIP, sourceActiveParticipant.getNetworkAccessPointID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS), 
                sourceActiveParticipant.getNetworkAccessPointID());
        }
        assertEquals(AuditTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME, 
            sourceActiveParticipant.getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE, 
            sourceActiveParticipant.getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME, 
            sourceActiveParticipant.getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_SOURCE_DISPLAY_NAME, 
            sourceActiveParticipant.getRoleIDCode().get(0).getDisplayName());
    }

    /**
     * Test of getActiveParticipantDestination method, of class AuditTransform.
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
            if (item.getRoleIDCode().get(0).getDisplayName() != null && item.getRoleIDCode().get(0).
                getDisplayName().equals(AuditTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME)) {
                destinationActiveParticipant = item;
            }
        }
        if (isRequesting) {
            assertEquals(remoteObjectIP, destinationActiveParticipant.getUserID());
        } else {
            assertEquals(webContextProperties.getProperty(NhincConstants.WEB_SERVICE_REQUEST_URL), 
                destinationActiveParticipant.getUserID());
        }

        assertEquals(!(isRequesting), destinationActiveParticipant.isUserIsRequestor());
        assertEquals(webContextProperties.getProperty(NhincConstants.REMOTE_HOST_ADDRESS), 
            destinationActiveParticipant.getNetworkAccessPointID());
        assertEquals(AuditTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME, destinationActiveParticipant.
            getNetworkAccessPointTypeCode());
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST, destinationActiveParticipant.
            getRoleIDCode().get(0).getCode());
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_CODE_SYSTEM_NAME, destinationActiveParticipant.
            getRoleIDCode().get(0).getCodeSystemName());
        assertEquals(AuditTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME, 
            destinationActiveParticipant.getRoleIDCode().get(0).getDisplayName());
    }

}
