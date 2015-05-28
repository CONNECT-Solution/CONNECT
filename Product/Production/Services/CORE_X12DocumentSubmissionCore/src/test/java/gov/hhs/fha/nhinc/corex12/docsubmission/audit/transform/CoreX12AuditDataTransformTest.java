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
package gov.hhs.fha.nhinc.corex12.docsubmission.audit.transform;

import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.corex12.docsubmission.audit.CORE_X12AuditDataTransformConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformConstants;
import gov.hhs.fha.nhinc.transform.audit.AuditDataTransformHelper;
import java.lang.management.ManagementFactory;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author nsubrama
 */
public class CoreX12AuditDataTransformTest {

    private Properties webContextProeprties = null;
    private CORE_X12AuditDataTransform coreX12AuditDataTransform = null;

    public CoreX12AuditDataTransformTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of transformX12MsgToAuditMsg method, of class CoreX12AuditDataTransform.
     */
    @Test
    public void testGetWebServiceRequestURL() {
        String serviceUrl = "http://localhost:8181/testservice";
        //Bad case if the webContextProeprties is null
        webContextProeprties = null;
        assertEquals(coreX12AuditDataTransform.getWebServiceRequestURL(webContextProeprties), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        //Bad case if the webContextProeprties is empty
        webContextProeprties = new Properties();
        assertEquals(coreX12AuditDataTransform.getWebServiceRequestURL(webContextProeprties), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        //Good case
        webContextProeprties.put(NhincConstants.WEB_SERVICE_REQUEST_URL, serviceUrl);
        assertEquals(coreX12AuditDataTransform.getWebServiceRequestURL(webContextProeprties), serviceUrl);
    }

    @Test
    public void testGetWebServiceUrlFromRemoteObject() throws ConnectionManagerException {

        String serviceUrl = "http://localhost:8181/testservice";
        NhinTargetSystemType nhinTargetSystemType = mock(NhinTargetSystemType.class);
        final ConnectionManagerCache connectionManagerCache = mock(ConnectionManagerCache.class);
        //Case1: If the target is null
        assertEquals(coreX12AuditDataTransform.getWebServiceUrlFromRemoteObject(null, null), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        //Case2: If valid value is passed
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform() {
            @Override
            protected ConnectionManagerCache getConnectionManagerCache() {
                return connectionManagerCache;
            }
        };
        when(connectionManagerCache.getEndpointURLFromNhinTarget(Mockito.any(NhinTargetSystemType.class), Mockito.anyString())).thenReturn(serviceUrl);
        assertEquals(coreX12AuditDataTransform.getWebServiceUrlFromRemoteObject(nhinTargetSystemType, "Testing"), serviceUrl);
    }

    @Test
    public void testGetNetworkAccessPointTypeCode() {
        String hostAddress = "192.1.1.1";
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform();
        //IP address
        assertEquals(coreX12AuditDataTransform.getNetworkAccessPointTypeCode(hostAddress), AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        hostAddress = "www.abcd.com";
        //Host Name
        assertEquals(coreX12AuditDataTransform.getNetworkAccessPointTypeCode(hostAddress), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
        //startswith ip + hostname
        hostAddress = "192.168.20.4.abcd.com";
        assertEquals(coreX12AuditDataTransform.getNetworkAccessPointTypeCode(hostAddress), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
    }

    @Test
    public void testGetRemoteHostAddress() {
        String hostName = "www.test.com";
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform();
        //Bad case if the webContextProeprties is null
        webContextProeprties = null;
        assertEquals(coreX12AuditDataTransform.getRemoteHostAddress(null), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS);
        //Bad case if the webContextProeprties is empty
        webContextProeprties = new Properties();
        assertEquals(coreX12AuditDataTransform.getRemoteHostAddress(null), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS);
        //Good case
        webContextProeprties.put(NhincConstants.REMOTE_HOST_ADDRESS, hostName);
        assertEquals(coreX12AuditDataTransform.getRemoteHostAddress(webContextProeprties), hostName);
    }

    @Test
    public void testGetActiveParticipantDestination() {
        final String hostURL1 = "http://192.168.3.4:9090/testNwhinService";
        final String hostURL2 = "http://hello.org/testNwhinService";
        //Negative case the URL is null if its from the requesting gateway
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform() {
            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return null;
            }

            @Override
            protected String getWebServiceRequestURL(Properties webContextProeprties) {
                return null;
            }
        };
        AuditMessageType.ActiveParticipant activeParticipant = coreX12AuditDataTransform.getActiveParticipantDestination(null, true, null, null);
        assertEquals(activeParticipant.getUserID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        assertEquals(activeParticipant.getNetworkAccessPointID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME);

        //Negative case the URL is null if its from the responding gateway
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform() {
            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return null;
            }

            @Override
            protected String getWebServiceRequestURL(Properties webContextProeprties) {
                return null;
            }
        };
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantDestination(null, false, null, null);
        assertEquals(activeParticipant.getUserID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        assertEquals(activeParticipant.getNetworkAccessPointID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME);

        //Positive case requesting gateway
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform() {
            @Override
            protected String getWebServiceUrlFromRemoteObject(NhinTargetSystemType target, String serviceName) {
                return hostURL1;
            }

            @Override
            protected String getWebServiceRequestURL(Properties webContextProeprties) {
                return hostURL2;
            }
        };
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantDestination(null, true, null, null);
        assertEquals(activeParticipant.getUserID(), hostURL1);
        assertEquals(activeParticipant.getNetworkAccessPointID(), "192.168.3.4");
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME);
        //Positive case responding gateway
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantDestination(null, false, null, null);
        assertEquals(activeParticipant.getUserID(), hostURL2);
        assertEquals(activeParticipant.getNetworkAccessPointID(), "hello.org");
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICIPANT_ROLE_CODE_DEST);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_DESTINATION_DISPLAY_NAME);
    }

    @Test
    public void testGetActiveParticipantSource() {
        final String hostName1 = "192.1.1.1";
        final String hostName2 = "nhin.test.org";
        final String localIP = "10.0.0.1";
        String alternateUserID = ManagementFactory.getRuntimeMXBean().getName();
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform() {
            @Override
            protected String getLocalHostAddress() {
                return localIP;
            }
        };

        //Negative test case Initiating gateway
        webContextProeprties = new Properties();
        AuditMessageType.ActiveParticipant activeParticipant = coreX12AuditDataTransform.getActiveParticipantSource(Boolean.TRUE, webContextProeprties);
        assertEquals(activeParticipant.getUserID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        assertEquals(activeParticipant.getAlternativeUserID(), alternateUserID);
        assertEquals(activeParticipant.getNetworkAccessPointID(), localIP);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.TRUE);
        //Negative test case Responding gateway
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantSource(Boolean.FALSE, webContextProeprties);
        assertEquals(activeParticipant.getUserID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        assertEquals(activeParticipant.getAlternativeUserID(), alternateUserID);
        assertEquals(activeParticipant.getNetworkAccessPointID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_UNKNOWN_IP_ADDRESS);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);

        //Valid case with ip address Initiating gateway
        webContextProeprties.put(NhincConstants.REMOTE_HOST_ADDRESS, hostName1);
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantSource(Boolean.TRUE, webContextProeprties);
        assertEquals(activeParticipant.getUserID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        assertEquals(activeParticipant.getAlternativeUserID(), alternateUserID);
        assertEquals(activeParticipant.getNetworkAccessPointID(), localIP);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.TRUE);

        //Valid case with ip address Responding gateway
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantSource(Boolean.FALSE, webContextProeprties);
        assertEquals(activeParticipant.getUserID(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_USER_ID_SOURCE);
        assertEquals(activeParticipant.getAlternativeUserID(), alternateUserID);
        assertEquals(activeParticipant.getNetworkAccessPointID(), hostName1);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);

        //Valid case with domain name Initiating gateway
        webContextProeprties.put(NhincConstants.REMOTE_HOST_ADDRESS, hostName2);
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantSource(Boolean.TRUE, webContextProeprties);
        assertEquals(activeParticipant.getAlternativeUserID(), alternateUserID);
        assertEquals(activeParticipant.getNetworkAccessPointID(), localIP);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_IP);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.TRUE);

        //Valid case with domain name Responding gateway
        webContextProeprties.put(NhincConstants.REMOTE_HOST_ADDRESS, hostName2);
        activeParticipant = coreX12AuditDataTransform.getActiveParticipantSource(Boolean.FALSE, webContextProeprties);
        assertEquals(activeParticipant.getAlternativeUserID(), alternateUserID);
        assertEquals(activeParticipant.getNetworkAccessPointID(), hostName2);
        assertEquals(activeParticipant.getNetworkAccessPointTypeCode(), CORE_X12AuditDataTransformConstants.NETWORK_ACCESSOR_PT_TYPE_CODE_NAME);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCode(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_CDE);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(activeParticipant.getRoleIDCode().get(0).getDisplayName(), CORE_X12AuditDataTransformConstants.ACTIVE_PARTICPANT_ROLE_CODE_SOURCE_DISPLAY_NAME);
        assertEquals(activeParticipant.isUserIsRequestor(), Boolean.FALSE);
    }

    @Test
    public void testGetEventIdentificationType() {
        coreX12AuditDataTransform = new CORE_X12AuditDataTransform();
        //Initiating gateway
        CodedValueType eventId = AuditDataTransformHelper.createCodeValueType(CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT);
        EventIdentificationType oEventIdentificationType = coreX12AuditDataTransform.getEventIdentificationType(eventId, true);
        assertEquals(oEventIdentificationType.getEventActionCode(), AuditDataTransformConstants.EVENT_ACTION_CODE_READ);
        assertEquals(oEventIdentificationType.getEventOutcomeIndicator().toString(), AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS.toString());
        assertEquals(oEventIdentificationType.getEventID().getCode(), CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_X12);
        assertEquals(oEventIdentificationType.getEventID().getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(oEventIdentificationType.getEventID().getDisplayName(), CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_EXPORT);

        //Responding gateway
        eventId = AuditDataTransformHelper.createCodeValueType(CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_X12, null,
            AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC, CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_IMPORT);
        oEventIdentificationType = coreX12AuditDataTransform.getEventIdentificationType(eventId, false);
        assertEquals(oEventIdentificationType.getEventActionCode(), AuditDataTransformConstants.EVENT_ACTION_CODE_CREATE);
        assertEquals(oEventIdentificationType.getEventOutcomeIndicator().toString(), AuditDataTransformConstants.EVENT_OUTCOME_INDICATOR_SUCCESS.toString());
        assertEquals(oEventIdentificationType.getEventID().getCode(), CORE_X12AuditDataTransformConstants.EVENT_ID_CODE_X12);
        assertEquals(oEventIdentificationType.getEventID().getCodeSystemName(), AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_DOC);
        assertEquals(oEventIdentificationType.getEventID().getDisplayName(), CORE_X12AuditDataTransformConstants.EVENT_ID_DISPLAY_NAME_X12_IMPORT);
    }
}
