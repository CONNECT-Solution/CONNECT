/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;

/**
 *
 * @author dunnek
 */
public class XDRHelperTest {
    private Mockery context;

    public XDRHelperTest() {
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

    /**
     * Test of createErrorResponse method, of class XDRHelper.
     */
    @Test
    public void testCreateErrorResponse() {
        /*
        System.out.println("createErrorResponse");
        RegistryErrorList errorList = null;
        XDRHelper instance = new XDRHelper();
        RegistryResponseType expResult = null;
        RegistryResponseType result = instance.createErrorResponse(errorList);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
         * */
    }

    /**
     * Test of validateDocumentMetaData method, of class XDRHelper.
     */
    @Test
    public void testValidateDocumentMetaData_Null() {
        System.out.println("validateDocumentMetaData");
        ProvideAndRegisterDocumentSetRequestType body = null;
        XDRHelper instance = createHelper();

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(XDRHelper.XDR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSMissingDocument, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());

    }
    @Test
    public void testValidateDocumentMetaData_ValidMessage() {
        System.out.println("testValidateDocumentMetaData_ValidMessage");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();
        XDRHelper instance = createHelper();


        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(0, result.getRegistryError().size());


    }
    @Test
    public void testValidateDocumentMetaData_MissingDoc() {
        System.out.println("testValidateDocumentMetaData_ValidMessage");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();

        body.getDocument().clear();
        XDRHelper instance = createHelper();

        
        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(XDRHelper.XDR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSMissingDocument, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());

    }
    @Test
    public void testValidateDocumentMetaData_UnsupportedMimeType() {
        System.out.println("testValidateDocumentMetaData_UnsupportedMimeType");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();

        XDRHelper instance = createHelper(false);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(XDRHelper.XDR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSRegistryMetadataError, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());
    }

    @Test
    public void testValidateDocumentMetaData_SupportedMimeType() {
        System.out.println("testValidateDocumentMetaData_SupportedMimeType");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();

        XDRHelper instance = createHelper(true);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(0, result.getRegistryError().size());
    }
    @Test
    public void testValidateDocumentMetaData_PatIdsNoMatch() {
        System.out.println("testValidateDocumentMetaData_PatIdsNoMatch");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage("ProvideAndRegisterDocumentSet-MultiPat.xml");


        XDRHelper instance = createHelper(true);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(XDRHelper.XDR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSPatientIdDoesNotMatch, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());
    }
    private XDRHelper createHelper()
    {
        final Log mockLogger = context.mock(Log.class);
        //TestHelper helper = new TestHelper();

        XDRHelper result = new XDRHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected boolean checkIdsMatch()
            {
                return true;
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
    private XDRHelper createHelper(boolean supportedMimeType)
    {
        final Log mockLogger = context.mock(Log.class);
        final boolean boolResult = supportedMimeType;
        //TestHelper helper = new TestHelper();

        XDRHelper result = new XDRHelper() {

            @Override
            protected Log createLogger() {
                return mockLogger;
            }
            @Override
            protected boolean isSupportedMimeType(String mimeType) {
                return boolResult;
            }
            @Override
            protected boolean checkIdsMatch()
            {
                return true;
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

}