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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author dunnek
 */
public class XDRTransformsTest {
    private static final String CONST_USER_NAME = XDRMessageHelper.CONST_USER_NAME;
    private static final String CONST_HCID = "1.1";

    public XDRTransformsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTransformResponseToAuditMsg_Null() {
        System.out.println("testTransformResponseToAuditMsg_Null");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType result = instance.transformResponseToAuditMsg(null, assertion, null, direction, _interface, false);
        assertNull(result);

    }

    @Test
    public void testTransformResponseToAuditMsg_Empty() {
        System.out.println("testTransformResponseToAuditMsg_Empty");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        NhinTargetSystemType target = new NhinTargetSystemType();
        LogEventRequestType result = instance.transformResponseToAuditMsg(new RegistryResponseType(), assertion,
                target, direction, _interface, false);
        assertNotNull(result);

    }

    @Test
    public void testTransformResponseToAuditMsg_NotEmpty() {
        System.out.println("testTransformResponseToAuditMsg_Empty");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        RegistryResponseType response = new RegistryResponseType();
        NhinTargetSystemType target = new NhinTargetSystemType();

        LogEventRequestType result = instance.transformResponseToAuditMsg(response, assertion, target, direction,
                _interface, false);
        assertNotNull(result);
        assertEquals(_interface, result.getInterface());
        assertEquals(direction, result.getDirection());

    }

    @Test
    public void testTransformResponseToAuditMsg_Success() {
        System.out.println("testTransformResponseToAuditMsg_Empty");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus("Success");
        NhinTargetSystemType target = new NhinTargetSystemType();
        LogEventRequestType result = instance.transformResponseToAuditMsg(response, assertion, target, direction, _interface, false);
        assertNotNull(result);
        assertEquals(_interface, result.getInterface());
        assertEquals(direction, result.getDirection());

    }

    /**
     * Test of transformRequestToAuditMsg method, of class XDRTransforms.
     */
    @Test
    public void testTransformRequestToAuditMsg_XDS_null() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = null;
        AssertionType assertion = null;
        String direction = "";
        String _interface = "";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, null, direction, _interface);
        assertNull(result);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testTransformRequestToAuditMsg_XDS_empty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = createAssertion();
        String direction = "";
        String _interface = "";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        NhinTargetSystemType target = new NhinTargetSystemType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, target, direction, _interface);

        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testTransformRequestToAuditMsg_XDS_NotEmpty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new XDRMessageHelper().getSampleMessage();
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;     
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        NhinTargetSystemType target = createNhinTargetSystem(CONST_HCID);
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, target, direction, _interface);

        assertNotNull(result);
        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
        assertNotNull(result.getAuditMessage().getActiveParticipant());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());
        assertEquals(1, result.getAuditMessage().getAuditSourceIdentification().size());

        assertEquals(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, result.getDirection());
        assertEquals(_interface, result.getInterface());

        assertEquals(CONST_USER_NAME, result.getAuditMessage().getActiveParticipant().get(0).getUserID());

        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0)
                .getAuditEnterpriseSiteID());
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testTransformProxyRequestToAuditMsg_XDS_null() {

        System.out.println("transformRequestToAuditMsg");
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = null;
        AssertionType assertion = null;
        String direction = "";
        String _interface = "";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        NhinTargetSystemType target = new NhinTargetSystemType();
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, target, direction, _interface);
        assertNull(result);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testTransformProxyRequestToAuditMsg_XDS_empty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        proxyRequest.setProvideAndRegisterDocumentSetRequest(request);

        AssertionType assertion = createAssertion();
        String direction = "";
        String _interface = "";
        NhinTargetSystemType target = new NhinTargetSystemType();
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType result = instance
                .transformRequestToAuditMsg(proxyRequest, assertion, target, direction, _interface);

        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testTransformProxyRequestToAuditMsg_XDS_NotEmpty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new XDRMessageHelper().getSampleMessage();
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType proxyRequest = new gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        proxyRequest.setProvideAndRegisterDocumentSetRequest(request);

        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION;
        String _interface = NhincConstants.AUDIT_LOG_NHIN_INTERFACE;
        NhinTargetSystemType target = createNhinTargetSystem(CONST_HCID);
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType result = instance
                .transformRequestToAuditMsg(proxyRequest, assertion, target, direction, _interface);

        assertNotNull(result);
        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
        assertNotNull(result.getAuditMessage().getActiveParticipant());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());
        assertEquals(1, result.getAuditMessage().getAuditSourceIdentification().size());

        assertEquals(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, result.getDirection());
        assertEquals(_interface, result.getInterface());

        assertEquals(CONST_USER_NAME, result.getAuditMessage().getActiveParticipant().get(0).getUserID());

        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0)
                .getAuditEnterpriseSiteID());
        // TODO review the generated test code and remove the default call to fail.

    }

    @Test
    public void testTransformEntityRequestToAuditMsg_XDS_null() {

        System.out.println("transformRequestToAuditMsg");
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = null;
        AssertionType assertion = null;
        String direction = "";
        String _interface = "";
        NhinTargetSystemType target = new NhinTargetSystemType();
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, target, direction, _interface);
        assertNull(result);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.

    }

    /*
     * @Test public void testTransformEntityRequestToAuditMsg_XDS_empty() {
     * 
     * System.out.println("transformRequestToAuditMsg"); ProvideAndRegisterDocumentSetRequestType request = new
     * ProvideAndRegisterDocumentSetRequestType();
     * gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType
     * entityRequest = new
     * gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
     * entityRequest.setProvideAndRegisterDocumentSetRequest(request);
     * 
     * AssertionType assertion = createAssertion(); String direction = ""; String _interface = ""; XDRTransforms
     * instance = createTransformsClass_OverrideRequiredFields(); LogEventRequestType expResult = new
     * LogEventRequestType(); LogEventRequestType result = instance.transformRequestToAuditMsg(entityRequest, assertion,
     * direction, _interface);
     * 
     * assertNotNull(result); // TODO review the generated test code and remove the default call to fail.
     * 
     * }
     * 
     * @Test public void testTransformEntityRequestToAuditMsg_XDS_NotEmpty() {
     * 
     * System.out.println("transformRequestToAuditMsg"); ProvideAndRegisterDocumentSetRequestType request = new
     * XDRMessageHelper().getSampleMessage();
     * gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType
     * entityRequest = new
     * gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
     * entityRequest.setProvideAndRegisterDocumentSetRequest(request);
     * 
     * AssertionType assertion = createAssertion(); String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
     * String _interface = "interface"; XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
     * LogEventRequestType expResult = new LogEventRequestType(); LogEventRequestType result =
     * instance.transformRequestToAuditMsg(entityRequest, assertion, direction, _interface);
     * 
     * assertNotNull(result); assertNotNull(result.getAuditMessage());
     * assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
     * assertNotNull(result.getAuditMessage().getActiveParticipant());
     * assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
     * 
     * assertEquals(1,result.getAuditMessage().getActiveParticipant().size());
     * 
     * assertEquals(1, result.getAuditMessage().getActiveParticipant().size()); assertEquals(1,
     * result.getAuditMessage().getAuditSourceIdentification().size());
     * 
     * assertEquals(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, result.getDirection()); assertEquals(_interface,
     * result.getInterface());
     * 
     * assertEquals(CONST_USER_NAME,result.getAuditMessage().getActiveParticipant().get(0).getUserID());
     * 
     * assertNotNull(result.getAuditMessage()); assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
     * 
     * assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
     * assertEquals(CONST_HC_NAME,
     * result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID()); // TODO review the
     * generated test code and remove the default call to fail.
     * 
     * }
     */
    @Test
    public void testAreRequiredXDSfieldsNull_empty() {

        System.out.println("areRequiredXDSfieldsNull_empty");
        ProvideAndRegisterDocumentSetRequestType body = new ProvideAndRegisterDocumentSetRequestType();
        AssertionType assertion = new AssertionType();
        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        boolean expResult = true;
        boolean result = instance.areRequiredXDSfieldsNull(body, assertion);
        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredXDSfieldsNull_null() {

        System.out.println("areRequiredXDSfieldsNull_null");
        ProvideAndRegisterDocumentSetRequestType body = null;
        AssertionType assertion = new AssertionType();
        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        boolean expResult = true;
        boolean result = instance.areRequiredXDSfieldsNull(body, assertion);
        assertEquals(expResult, result);

    }

    /**
     * Test of areRequiredUserTypeFieldsNull method, of class XDRTransforms.
     */
    @Test
    public void testAreRequiredUserTypeFieldsNull_empty() {
        System.out.println("areRequiredUserTypeFieldsNull");
        AssertionType oAssertion = new AssertionType();
        XDRTransforms instance = createTransformsClass();
        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(oAssertion);
        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_null() {
        System.out.println("areRequiredUserTypeFieldsNull");
        AssertionType oAssertion = new AssertionType();
        XDRTransforms instance = createTransformsClass();
        boolean expResult = true;
        boolean result = instance.areRequiredUserTypeFieldsNull(oAssertion);
        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredUserTypeFieldsNull_NotEmpty() {
        System.out.println("areRequiredUserTypeFieldsNull");

        AssertionType assertion = createAssertion();

        XDRTransforms instance = createTransformsClass();
        boolean expResult = false;
        boolean result = instance.areRequiredUserTypeFieldsNull(assertion);
        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredResponseFieldsNull_Null() {
        System.out.println("testAreRequiredResponseFieldsNull_Null");

        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        AssertionType assertion = createAssertion();

        boolean expResult = true;
        boolean result = instance.areRequiredResponseFieldsNull(null, assertion);

        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredResponseFieldsNull_Empty() {
        System.out.println("testAreRequiredResponseFieldsNull_Empty");

        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        AssertionType assertion = createAssertion();

        boolean expResult = true;
        boolean result = instance.areRequiredResponseFieldsNull(new RegistryResponseType(), assertion);

        assertEquals(expResult, result);

    }

    @Test
    public void testAreRequiredResponseFieldsNull_NotEmpty() {
        System.out.println("testAreRequiredResponseFieldsNull_NotEmpty");

        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        AssertionType assertion = createAssertion();

        RegistryResponseType response = new RegistryResponseType();

        response.setStatus("Success");

        boolean expResult = false;
        boolean result = instance.areRequiredResponseFieldsNull(response, assertion);

        assertEquals(expResult, result);

    }

    private XDRTransforms createTransformsClass() {
        // TestHelper helper = new TestHelper();

        XDRTransforms result = new XDRTransforms();
        
        return result;
    }

    private XDRTransforms createTransformsClass_OverrideUserTypeCheck() {
        // TestHelper helper = new TestHelper();

        XDRTransforms result = new XDRTransforms() {

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

        };
       
        return result;
    }

    private XDRTransforms createTransformsClass_OverrideRequiredFields() {
        // TestHelper helper = new TestHelper();

        XDRTransforms result = new XDRTransforms() {

            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

            @Override
            protected boolean areRequiredXDSfieldsNull(ProvideAndRegisterDocumentSetRequestType body,
                    AssertionType assertion) {
                return false;
            }

            @Override
            protected boolean areRequiredResponseFieldsNull(RegistryResponseType response, AssertionType assertion) {
                return false;
            }

        };
        
        return result;
    }

    private AssertionType createAssertion() {
        return new XDRMessageHelper().createAssertion(CONST_HCID);
    }
    
    private NhinTargetSystemType createNhinTargetSystem(String hcid) {
        return new XDRMessageHelper().createNhinTargetSystem(hcid);
    }

}