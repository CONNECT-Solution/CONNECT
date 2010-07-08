/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.UserType;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import com.services.nhinc.schema.auditmessage.AuditMessageType.ActiveParticipant;
import com.services.nhinc.schema.auditmessage.AuditMessageType;
import com.services.nhinc.schema.auditmessage.AuditSourceIdentificationType;
import com.services.nhinc.schema.auditmessage.CodedValueType;
import com.services.nhinc.schema.auditmessage.EventIdentificationType;
import com.services.nhinc.schema.auditmessage.ParticipantObjectIdentificationType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
/**
 *
 * @author dunnek
 */
public class XDRTransformsTest {
    private Mockery context;
    private static final String CONST_USER_NAME = "userName";
    private static final String CONST_USER_ID = "userId";
    private static final String CONST_HCID = "1.1";
    private static final String CONST_HC_NAME = "Home COmmunity";
    private static final String CONST_HC_DESC = "HC Description";
    
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
    public void testTransformResponseToAuditMsg_Null()
    {
        System.out.println("testTransformResponseToAuditMsg_Null");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType result = instance.transformResponseToAuditMsg(null, assertion, direction, _interface);
        assertNull(result);

    }
     @Test
    public void testTransformResponseToAuditMsg_Empty()
    {
        System.out.println("testTransformResponseToAuditMsg_Empty");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType result = instance.transformResponseToAuditMsg(new RegistryResponseType(), assertion, direction, _interface);
        assertNotNull(result);

    }
     @Test
    public void testTransformResponseToAuditMsg_NotEmpty()
    {
        System.out.println("testTransformResponseToAuditMsg_Empty");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        RegistryResponseType response = new RegistryResponseType();
        
        LogEventRequestType result = instance.transformResponseToAuditMsg(response, assertion, direction, _interface);
        assertNotNull(result);
        assertEquals(_interface, result.getInterface());
        assertEquals(direction, result.getDirection());

    }
     @Test
    public void testTransformResponseToAuditMsg_Success()
    {
        System.out.println("testTransformResponseToAuditMsg_Empty");
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        RegistryResponseType response = new RegistryResponseType();
        response.setStatus("Success");
        LogEventRequestType result = instance.transformResponseToAuditMsg(response, assertion, direction, _interface);
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
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, direction, _interface);
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
        LogEventRequestType expResult = new LogEventRequestType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, direction, _interface);
        
        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }
    @Test
    public void testTransformRequestToAuditMsg_XDS_NotEmpty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new XDRMessageHelper().getSampleMessage();
        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = new LogEventRequestType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, direction, _interface);

        assertNotNull(result);
        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
        assertNotNull(result.getAuditMessage().getActiveParticipant());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(1,result.getAuditMessage().getActiveParticipant().size());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());
        assertEquals(1, result.getAuditMessage().getAuditSourceIdentification().size());

        assertEquals(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, result.getDirection());
        assertEquals(_interface, result.getInterface());
             
        assertEquals(CONST_USER_NAME,result.getAuditMessage().getActiveParticipant().get(0).getUserID());

        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
        assertEquals(CONST_HC_NAME, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
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
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, direction, _interface);
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
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = new LogEventRequestType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(proxyRequest, assertion, direction, _interface);

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
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = new LogEventRequestType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(proxyRequest, assertion, direction, _interface);

        assertNotNull(result);
        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
        assertNotNull(result.getAuditMessage().getActiveParticipant());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(1,result.getAuditMessage().getActiveParticipant().size());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());
        assertEquals(1, result.getAuditMessage().getAuditSourceIdentification().size());

        assertEquals(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, result.getDirection());
        assertEquals(_interface, result.getInterface());

        assertEquals(CONST_USER_NAME,result.getAuditMessage().getActiveParticipant().get(0).getUserID());

        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
        assertEquals(CONST_HC_NAME, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        // TODO review the generated test code and remove the default call to fail.

    }

   @Test
    public void testTransformEntityRequestToAuditMsg_XDS_null() {

        System.out.println("transformRequestToAuditMsg");
        gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = null;
        AssertionType assertion = null;
        String direction = "";
        String _interface = "";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = null;
        LogEventRequestType result = instance.transformRequestToAuditMsg(request, assertion, direction, _interface);
        assertNull(result);
        assertEquals(expResult, result);

        // TODO review the generated test code and remove the default call to fail.

    }
   /*
    @Test
    public void testTransformEntityRequestToAuditMsg_XDS_empty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType entityRequest = new gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        entityRequest.setProvideAndRegisterDocumentSetRequest(request);
        
        AssertionType assertion = createAssertion();
        String direction = "";
        String _interface = "";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = new LogEventRequestType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(entityRequest, assertion, direction, _interface);

        assertNotNull(result);
        // TODO review the generated test code and remove the default call to fail.

    }
    @Test
    public void testTransformEntityRequestToAuditMsg_XDS_NotEmpty() {

        System.out.println("transformRequestToAuditMsg");
        ProvideAndRegisterDocumentSetRequestType request = new XDRMessageHelper().getSampleMessage();
        gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType entityRequest = new gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        entityRequest.setProvideAndRegisterDocumentSetRequest(request);

        AssertionType assertion = createAssertion();
        String direction = NhincConstants.AUDIT_LOG_INBOUND_DIRECTION;
        String _interface = "interface";
        XDRTransforms instance = createTransformsClass_OverrideRequiredFields();
        LogEventRequestType expResult = new LogEventRequestType();
        LogEventRequestType result = instance.transformRequestToAuditMsg(entityRequest, assertion, direction, _interface);

        assertNotNull(result);
        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());
        assertNotNull(result.getAuditMessage().getActiveParticipant());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(1,result.getAuditMessage().getActiveParticipant().size());

        assertEquals(1, result.getAuditMessage().getActiveParticipant().size());
        assertEquals(1, result.getAuditMessage().getAuditSourceIdentification().size());

        assertEquals(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, result.getDirection());
        assertEquals(_interface, result.getInterface());

        assertEquals(CONST_USER_NAME,result.getAuditMessage().getActiveParticipant().get(0).getUserID());

        assertNotNull(result.getAuditMessage());
        assertNotNull(result.getAuditMessage().getAuditSourceIdentification());

        assertEquals(CONST_HCID, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditSourceID());
        assertEquals(CONST_HC_NAME, result.getAuditMessage().getAuditSourceIdentification().get(0).getAuditEnterpriseSiteID());
        // TODO review the generated test code and remove the default call to fail.

    }
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
    public void testAreRequiredResponseFieldsNull_Null()
    {
        System.out.println("testAreRequiredResponseFieldsNull_Null");

        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        AssertionType assertion = createAssertion();
        
        boolean expResult = true;
        boolean result = instance.areRequiredResponseFieldsNull(null, assertion);

        assertEquals(expResult, result);

    }
     @Test
    public void testAreRequiredResponseFieldsNull_Empty()
    {
        System.out.println("testAreRequiredResponseFieldsNull_Empty");

        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        AssertionType assertion = createAssertion();

        boolean expResult = true;
        boolean result = instance.areRequiredResponseFieldsNull(new RegistryResponseType(), assertion);

        assertEquals(expResult, result);

    }
    @Test
    public void testAreRequiredResponseFieldsNull_NotEmpty()
    {
        System.out.println("testAreRequiredResponseFieldsNull_NotEmpty");

        XDRTransforms instance = createTransformsClass_OverrideUserTypeCheck();
        AssertionType assertion = createAssertion();

        RegistryResponseType response = new RegistryResponseType();

        response.setStatus("Success");
        
        boolean expResult = false;
        boolean result = instance.areRequiredResponseFieldsNull(response, assertion);

        assertEquals(expResult, result);

    }


    private XDRTransforms createTransformsClass()
    {
        final Log mockLogger = context.mock(Log.class);
        //TestHelper helper = new TestHelper();

        XDRTransforms result = new XDRTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                //never(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        return result;
    }
    private XDRTransforms createTransformsClass_OverrideUserTypeCheck()
    {
        final Log mockLogger = context.mock(Log.class);
        //TestHelper helper = new TestHelper();

        XDRTransforms result = new XDRTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                //never(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        return result;
    }
    private XDRTransforms createTransformsClass_OverrideRequiredFields()
    {
        final Log mockLogger = context.mock(Log.class);
        //TestHelper helper = new TestHelper();

        XDRTransforms result = new XDRTransforms() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected boolean areRequiredUserTypeFieldsNull(AssertionType oAssertion) {
                return false;
            }
            @Override
            protected boolean areRequiredXDSfieldsNull(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                return false;
            }
            @Override
            protected boolean areRequiredResponseFieldsNull(RegistryResponseType response, AssertionType assertion)
            {
                return false;
            }

        };
        context.checking(new Expectations() {

            {
                allowing(mockLogger).info(with(any(String.class)));
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
                //never(mockLogger).error("Error");
                will(returnValue(null));
            }
        });
        return result;
    }
    private HomeCommunityType createHomeCommunity()
    {
        HomeCommunityType hc = new HomeCommunityType();
        hc.setDescription(CONST_HC_DESC);
        hc.setHomeCommunityId(CONST_HCID);
        hc.setName(CONST_HC_NAME);

        return hc;
    }
    private AssertionType createAssertion()
    {
        AssertionType assertion = new AssertionType();
        assertion.setUserInfo(new UserType());

        assertion.getUserInfo().setUserName(CONST_USER_NAME);        

        assertion.getUserInfo().setOrg(createHomeCommunity());

        return assertion;
    }
    private ProvideAndRegisterDocumentSetRequestType createProvideAndRegisterRequest()
    {
        ProvideAndRegisterDocumentSetRequestType result = new ProvideAndRegisterDocumentSetRequestType();

        return result;
    }

}