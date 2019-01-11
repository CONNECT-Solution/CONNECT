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

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dunnek
 */
public class AdminDistTransformsTest {

    public AdminDistTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Tests for null audit message if EDXLDistribution is null.
     */
    @Test
    public void testTransformEDXLDistributionRequestToAuditMsg_Empty() {
        EDXLDistribution message = new EDXLDistribution();
        AssertionType assertion = new AssertionType();

        AdminDistTransforms instance = new AdminDistTransforms();

        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformEDXLDistributionRequestToAuditMsg(message, assertion,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        assertEquals(expResult, result);

    }

    /**
     * Test the HCID can be pulled from the local community for INBOUND ENTITY in the audit message.
     */
    @Test
    public void testTransformEDXLDistributionRequestToAuditMsg_Good_HCIDfromLocalHCID() {
        final String LOCAL_HCID = "1.1";
        EDXLDistribution message = new EDXLDistribution();
        AssertionType assertion = new AssertionType();

        setUserForAssertion(assertion, LOCAL_HCID);

        AdminDistTransforms instance = new AdminDistTransforms() {
            @Override
            protected String getHomeCommunityFromMapping() {
                return LOCAL_HCID;
            }
        };

        LogEventRequestType result = instance.transformEDXLDistributionRequestToAuditMsg(message, assertion,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);

        assertNotNull(result);
        assertEquals(NhincConstants.AUDIT_LOG_ENTITY_INTERFACE + " " + NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
            result.getDirection());

        assertNotNull(result.getAuditMessage());
        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());

        assertNotNull(result.getAuditMessage().getEventIdentification());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, result.getAuditMessage()
            .getEventIdentification().getEventID().getCode());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, result.getAuditMessage()
            .getEventIdentification().getEventID().getCodeSystemName());
        assertEquals(result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID(), LOCAL_HCID);

    }

    /**
     * Test the HCID can be pulled from the assertion for INBOUND NHIN in the audit message.
     */
    @Test
    public void testTransformEDXLDistributionRequestToAuditMsg_Good_HCIDfromAssertion() {
        final String LOCAL_HCID = "1.1";
        EDXLDistribution message = new EDXLDistribution();
        AssertionType assertion = new AssertionType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(LOCAL_HCID);
        assertion.setHomeCommunity(homeCommunity);

        setUserForAssertion(assertion, LOCAL_HCID);

        AdminDistTransforms instance = new AdminDistTransforms();

        LogEventRequestType result = instance.transformEDXLDistributionRequestToAuditMsg(message, assertion,
            NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        assertNotNull(result);
        assertEquals(NhincConstants.AUDIT_LOG_NHIN_INTERFACE + " " + NhincConstants.AUDIT_LOG_INBOUND_DIRECTION,
            result.getDirection());

        assertNotNull(result.getAuditMessage());
        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());

        assertNotNull(result.getAuditMessage().getEventIdentification());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, result.getAuditMessage()
            .getEventIdentification().getEventID().getCode());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, result.getAuditMessage()
            .getEventIdentification().getEventID().getCodeSystemName());
        assertEquals(result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID(), LOCAL_HCID);

    }

    /**
     * Tests the HCID can be pulled from the target for NHINC OUTBOUND in the result message.
     */
    @Test
    public void testTransformEDXLDistributionRequestToAuditMsg_Good_HCIDfromTarget() {
        final String LOCAL_HCID = "2.2";
        EDXLDistribution message = new EDXLDistribution();
        AssertionType assertion = new AssertionType();

        setUserForAssertion(assertion, LOCAL_HCID);

        NhinTargetSystemType target = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(LOCAL_HCID);
        target.setHomeCommunity(homeCommunity);

        AdminDistTransforms instance = new AdminDistTransforms();

        LogEventRequestType result = instance.transformEDXLDistributionRequestToAuditMsg(message, assertion,
            target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

        assertNotNull(result);
        assertEquals(NhincConstants.AUDIT_LOG_NHIN_INTERFACE + " " + NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            result.getDirection());

        assertNotNull(result.getAuditMessage());
        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());

        assertNotNull(result.getAuditMessage().getEventIdentification());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, result.getAuditMessage()
            .getEventIdentification().getEventID().getCode());
        assertEquals(AuditDataTransformConstants.EVENT_ID_CODE_SYS_NAME_T63, result.getAuditMessage()
            .getEventIdentification().getEventID().getCodeSystemName());
        assertEquals(result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID(), LOCAL_HCID);

    }

    /**
     * Test of areRequiredUserTypeFieldsNull method, of class AdminDistTransforms.
     */
    @Test
    public void testAreRequiredUserTypeFieldsNull() {
        System.out.println("areRequiredUserTypeFieldsNull");

        AssertionType oAssertion = null;
        AdminDistTransforms instance = new AdminDistTransforms();

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(oAssertion);

        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_NoUserName() {
        System.out.println("testAreRequiredUserTypeFieldsNull_NoUserName");
        AssertionType assertion = new AssertionType();
        assertion.setUserInfo(new UserType());

        AdminDistTransforms instance = new AdminDistTransforms();

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);

        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_NoHCID() {
        System.out.println("testAreRequiredUserTypeFieldsNull_NoHCID");
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        user.setUserName("test");
        user.setOrg(hc);

        assertion.setUserInfo(user);

        AdminDistTransforms instance = new AdminDistTransforms();

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);

        assertEquals(expResult, result);
    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_NoHCName() {
        System.out.println("testAreRequiredUserTypeFieldsNull_NoHCName");
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId("123");

        user.setUserName("test");
        user.setOrg(hc);

        assertion.setUserInfo(user);

        AdminDistTransforms instance = new AdminDistTransforms();

        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);

        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_False() {
        System.out.println("testAreRequiredUserTypeFieldsNull_False");
        AssertionType assertion = new AssertionType();

        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId("123");
        hc.setName("test");

        user.setUserName("test");
        user.setOrg(hc);

        assertion.setUserInfo(user);

        AdminDistTransforms instance = new AdminDistTransforms();

        boolean expResult = false;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);

        assertEquals(expResult, result);

    }

    private void setUserForAssertion(AssertionType assertion, String communityId) {
        UserType user = new UserType();
        HomeCommunityType hc = new HomeCommunityType();

        hc.setHomeCommunityId(communityId);
        hc.setName("test");

        user.setUserName("test");

        user.setOrg(hc);

        assertion.setUserInfo(user);
    }
}
