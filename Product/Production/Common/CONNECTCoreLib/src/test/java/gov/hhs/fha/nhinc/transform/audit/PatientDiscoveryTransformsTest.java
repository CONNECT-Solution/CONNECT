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
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.PersonNameType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import junit.framework.Assert;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.MCCIMT000300UV01Sender;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201306UV02MFMIMT700711UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author shawc
 * 
 *         Note: requirements for the LogEventRequestType "Direction", required = true "Interface", required = true
 *         "EventIdentification", required = true "ActiveParticipant", required = true "AuditSourceIdentification",
 *         required = true "ParticipantObjectIdentification", required = false
 */
public class PatientDiscoveryTransformsTest {

    private Mockery context;

    public PatientDiscoveryTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setup() {
        context = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTransformNhinPRPAIN201305RequestToAuditMsgWillFailForNullRequest() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        testSubject.transformNhinPRPAIN201305RequestToAuditMsg(null, null, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_SYNC_TYPE,
                NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillFailForNullAssertionTypeParameter() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        testSubject.transformEntityPRPAIN201305RequestToAuditMsg(oPatientDiscoveryRequest, null,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE,
                NhincConstants.AUDIT_LOG_SYNC_TYPE, NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
        context.assertIsSatisfied();
    }

    @Test
    public void testTransformEntityPRPAIN201305RequestToAuditMsgWillPass() {
        RespondingGatewayPRPAIN201305UV02RequestType oPatientDiscoveryRequest = new RespondingGatewayPRPAIN201305UV02RequestType();
        AssertionType oAssertionType = new AssertionType();

        PRPAIN201305UV02 oPRPAIN201305UV = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        oPatientDiscoveryRequest.setAssertion(oAssertionType);
        oPatientDiscoveryRequest.setPRPAIN201305UV02(oPRPAIN201305UV);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage,
                    AssertionType oAssertion) {
                return false;
            }

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

            @Override
            protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage) {
                return mockListII.get(0);
            }
        };

        final LogEventRequestType expected = testSubject.transformEntityPRPAIN201305RequestToAuditMsg(
                oPatientDiscoveryRequest, oAssertionType, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_SYNC_TYPE,
                NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
        
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void testTransformNhinPRPAIN201305RequestToAuditMsgWillPass() {
        PRPAIN201305UV02 oPatientDiscoveryRequest = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(userInfo);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
        	@Override
            protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage,
                    AssertionType oAssertion) {
                return false;
            }

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

            @Override
            protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage) {
                return mockListII.get(0);
            }
        };

        final LogEventRequestType expected = testSubject.transformNhinPRPAIN201305RequestToAuditMsg(
                oPatientDiscoveryRequest, oAssertionType, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE, NhincConstants.AUDIT_LOG_SYNC_TYPE,
                NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
        
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void testTransformAdapterPRPAIN201305RequestToAuditMsgWillPass() {
        PRPAIN201305UV02 oPatientDiscoveryRequest = getTestPatientDiscoveryRequest();

        UserType userInfo = getTestUserType();
        AssertionType oAssertionType = new AssertionType();
        oAssertionType.setUserInfo(userInfo);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected boolean areRequired201305fieldsNull(PRPAIN201305UV02 oPatientDiscoveryRequestMessage,
                    AssertionType oAssertion) {
                return false;
            }

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

            @Override
            protected II getHL7IdentifiersFromRequest(PRPAIN201305UV02 oPatientDiscoveryRequestMessage) {
                return mockListII.get(0);
            }
        };

        final LogEventRequestType expected = testSubject.transformAdapterPRPAIN201305RequestToAuditMsg(
                oPatientDiscoveryRequest, oAssertionType, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, NhincConstants.AUDIT_LOG_SYNC_TYPE,
                NhincConstants.AUDIT_LOG_REQUEST_PROCESS);
       
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expected.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expected.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void testTransformEntityPRPAIN201306ResponseToAuditMsgMsgWillPass() {
        RespondingGatewayPRPAIN201306UV02ResponseType oPatientDiscoveryResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        AssertionType oAssertion = new AssertionType();
        CommunityPRPAIN201306UV02ResponseType communityResponse = new CommunityPRPAIN201306UV02ResponseType();

        MCCIMT000300UV01Sender sender = new MCCIMT000300UV01Sender();
        II typeId = new II();

        typeId.setRoot("2.16.840.1.113883.3.200");
        sender.setTypeId(typeId);

        // TODO mock the next 3 lines
        PRPAIN201306UV02 message = new PRPAIN201306UV02();
        message.setSender(sender);

        UserType userInfo = getTestUserType();
        oAssertion.setUserInfo(userInfo);

        communityResponse.setPRPAIN201306UV02(message);
        oPatientDiscoveryResponse.getCommunityResponse().add(communityResponse);

        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected List<II> getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage) {
                return mockListII;
            }

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

            @Override
            protected boolean areRequired201306fieldsNull(PRPAIN201306UV02 oPatientDiscoveryResponseMessage,
                    AssertionType oAssertion) {
                return false;
            }
        };

        LogEventRequestType expectedResult = testSubject.transformEntityPRPAIN201306ResponseToAuditMsg(
                oPatientDiscoveryResponse, oAssertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_SYNC_TYPE);

        Assert.assertNotNull(expectedResult);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification().getEventID());
    }

    @Test
    public void confirmTransformPRPAIN201306ResponseToAuditMsgDoesTransformation() {
        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected List<II> getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage) {
                return mockListII;
            }

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

            @Override
            protected boolean areRequired201306fieldsNull(PRPAIN201306UV02 oPatientDiscoveryResponseMessage,
                    AssertionType oAssertion) {
                return false;
            }
        };

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 pRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess pRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();

        pRPAIN201306UV.setControlActProcess(pRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        LogEventRequestType expectedResult = testSubject.transformPRPAIN201306ResponseToAuditMsg(pRPAIN201306UV,
                oAssertionType, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE,
                NhincConstants.AUDIT_LOG_SYNC_TYPE);

        Assert.assertNotNull(expectedResult);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getAuditSourceIdentification().size() == 1);
        Assert.assertTrue(expectedResult.getAuditMessage().getParticipantObjectIdentification().size() == 1);
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification());
        Assert.assertNotNull(expectedResult.getAuditMessage().getEventIdentification().getEventID());

    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForMissingUserName() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        userInfo.setUserName(null);
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage,
                oAssertionType);

        Assert.assertTrue(bExpectedResult);

    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForNullHL7Identifiers() {
        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage,
                oAssertionType);

        Assert.assertFalse(bExpectedResult);

    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForNullPatientId() {
       final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected List<II> getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage) {
                return mockListII;
            }

        };

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage,
                oAssertionType);

        Assert.assertTrue(bExpectedResult);
    }

    @Test
    public void testAreRequired201306fieldsNullWillFailForNullCommunityId() {
        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected List<II> getHL7IdentitiersFromResponse(PRPAIN201306UV02 oPatientDiscoveryResponseMessage) {
                return mockListII;
            }

        };

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201306UV02 oPatientDiscoveryResponseMessage = new PRPAIN201306UV02();

        boolean bExpectedResult = testSubject.areRequired201306fieldsNull(oPatientDiscoveryResponseMessage,
                oAssertionType);

        Assert.assertTrue(bExpectedResult);
    }

    @Test
    public void testAreRequiredUserTypeFieldsNullMethod() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        boolean bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);

        Assert.assertFalse(bExpectedResult);

        oAssertionType.getUserInfo().setUserName(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        Assert.assertTrue(bExpectedResult);

        oAssertionType.getUserInfo().setUserName("Test User");
        oAssertionType.getUserInfo().getOrg().setHomeCommunityId(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        Assert.assertTrue(bExpectedResult);
        
        oAssertionType.getUserInfo().setUserName("Test User");
        oAssertionType.getUserInfo().getOrg().setHomeCommunityId("2.16.840.1.113883.3.200");
        oAssertionType.getUserInfo().getOrg().setName(null);
        bExpectedResult = testSubject.areRequiredUserTypeFieldsNull(oAssertionType);
        Assert.assertTrue(bExpectedResult);
        
    }

    @Test
    public void testGetHL7IdentitiersFromResponseMethodWillFailForNullResponseRequest() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();
        
        List<II> oExpectedResult = testSubject.getHL7IdentitiersFromResponse(null);

        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentitiersFromResponseMethodWillFailForNullControlActProcess() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        List<II> oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);
        
        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentitiersFromResponseMethodWillFailForNullQueryByParameter() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        List<II> oExpectedResult = testSubject.getHL7IdentitiersFromResponse(oPRPAIN201306UV);

        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailForNullQueryByParameter() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory
                .createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        oPRPAIN201306UVMFMIMT700711UV01ControlActProcess.setQueryByParameter(oQueryByParameter);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(null);

        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailOnNullQueryByParameterGetValue() {
       PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory
                .createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        oQueryByParameter.setValue(null);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailOnNullParameterList() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory
                .createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        PRPAMT201306UV02QueryByParameter oParamList = new PRPAMT201306UV02QueryByParameter();
        oQueryByParameter.setValue(oParamList);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillFailForEmptyLivingSubjectId() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory
                .createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        PRPAMT201306UV02QueryByParameter oParamList = new PRPAMT201306UV02QueryByParameter();
        PRPAMT201306UV02ParameterList oPRPAMT201306UV02ParameterList = new PRPAMT201306UV02ParameterList();
        oPRPAMT201306UV02ParameterList.getLivingSubjectId().add(null);
        oParamList.setParameterList(oPRPAMT201306UV02ParameterList);

        oQueryByParameter.setValue(oParamList);
        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        Assert.assertNull(oExpectedResult);
    }

    @Test
    public void testGetHL7IdentifiersWillPass() {
        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms();

        final PRPAIN201306UV02 oPRPAIN201306UV = new PRPAIN201306UV02();
        final PRPAIN201306UV02MFMIMT700711UV01ControlActProcess oPRPAIN201306UVMFMIMT700711UV01ControlActProcess = new PRPAIN201306UV02MFMIMT700711UV01ControlActProcess();
        org.hl7.v3.ObjectFactory oJaxbObjectFactory = new org.hl7.v3.ObjectFactory();
        JAXBElement<PRPAMT201306UV02QueryByParameter> oQueryByParameter = oJaxbObjectFactory
                .createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(null);
        PRPAMT201306UV02QueryByParameter oParamList = new PRPAMT201306UV02QueryByParameter();
        // List<PRPAMT201306UV02LivingSubjectId> olLivingSubjectId = new ArrayList<PRPAMT201306UV02LivingSubjectId>();
        PRPAMT201306UV02ParameterList oPRPAMT201306UV02ParameterList = new PRPAMT201306UV02ParameterList();
        PRPAMT201306UV02LivingSubjectId oLivingSubject = new PRPAMT201306UV02LivingSubjectId();
        final String root = "1.1";
        final String extension = "1.234.567.8902";
        II oII = new II();
        oII.setRoot(root);
        oII.setExtension(extension);
        oLivingSubject.getValue().add(oII);
        oPRPAMT201306UV02ParameterList.getLivingSubjectId().add(oLivingSubject);
        oParamList.setParameterList(oPRPAMT201306UV02ParameterList);

        oQueryByParameter.setValue(oParamList);

        oPRPAIN201306UV.setControlActProcess(oPRPAIN201306UVMFMIMT700711UV01ControlActProcess);

        II oExpectedResult = testSubject.getHL7Identifiers(oQueryByParameter);

        Assert.assertNotNull(oExpectedResult);
        Assert.assertEquals(oExpectedResult.getRoot(), root);
        Assert.assertEquals(oExpectedResult.getExtension(), extension);
    }

    @Test
    public void testAreRequired201305fieldsNullMethod() {
        final List<II> mockListII = new ArrayList<II>();
        final II mockII = new II();
        mockII.setExtension("1111");
        mockII.setRoot("2.2.2.2");
        mockListII.add(mockII);

        PatientDiscoveryTransforms testSubject = new PatientDiscoveryTransforms() {
            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return true;
            }
        };

        AssertionType oAssertionType = new AssertionType();
        UserType userInfo = getTestUserType();
        oAssertionType.setUserInfo(userInfo);

        final PRPAIN201305UV02 oPatientDiscoveryRequestMessage = new PRPAIN201305UV02();

        boolean bExpectedResult = testSubject.areRequired201305fieldsNull(oPatientDiscoveryRequestMessage,
                oAssertionType);

        Assert.assertTrue(bExpectedResult);

    }

    @Test
    public void testAckToAuditTransfer() {
        PatientDiscoveryTransforms auditTransformer = new PatientDiscoveryTransforms();

        II msgId = new II();
        msgId.setExtension("12345");
        msgId.setRoot("2.2");
        MCCIIN000002UV01 ackMsg = HL7AckTransforms.createAckMessage("1.1.1", msgId, "CA", "Success", "1.1", "2.2");
        AssertionType assertion = new AssertionType();
        UserType userInfo = getTestUserType();
        assertion.setUserInfo(userInfo);

        LogEventRequestType auditMsg = auditTransformer.transformAck2AuditMsg(ackMsg, assertion,
                NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE);

        Assert.assertNotNull(auditMsg);
        Assert.assertEquals(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, auditMsg.getDirection());
        Assert.assertEquals(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE, auditMsg.getInterface());
        Assert.assertNotNull(auditMsg.getAuditMessage());
    }

    private UserType getTestUserType() {
        PersonNameType personName = new PersonNameType();
        HomeCommunityType home = new HomeCommunityType();
        UserType userInfo = new UserType();

        personName.setFamilyName("Kennedy");
        personName.setGivenName("John");
        userInfo.setPersonName(personName);
        home.setHomeCommunityId("2.16.840.1.113883.3.200");
        home.setName("Federal - VA");
        userInfo.setOrg(home);
        userInfo.setUserName("Test User");

        return userInfo;
    }

    private PRPAIN201305UV02 getTestPatientDiscoveryRequest() {
        MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
        II typeId = new II();
        typeId.setRoot("2.16.840.1.113883.3.200");
        typeId.setExtension("11111");
        sender.setTypeId(typeId);

        PRPAIN201305UV02 oPatientDiscoveryRequest = new PRPAIN201305UV02();
        oPatientDiscoveryRequest.setSender(sender);
        oPatientDiscoveryRequest.setTypeId(typeId);

        return oPatientDiscoveryRequest;
    }
}
