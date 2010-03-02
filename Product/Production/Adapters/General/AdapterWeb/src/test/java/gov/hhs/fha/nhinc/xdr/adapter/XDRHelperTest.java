/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.adapter;

import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
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
import org.junit.Ignore;

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
     * Test of validateDocumentMetaData method, of class XDRHelper.
     */
    @Ignore
    public void testgetSourcePatientId() {
        System.out.println("testgetSourcePatientId");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();
        XDRHelper instance = createHelper();


        String result = instance.getSourcePatientId(body);
        assertNotNull(result);
        assertEquals("ST-1000^^^&1.3.6.1.4.1.21367.2003.3.9&ISO", result);

    }
    @Test
    public void testValidateDocumentMetaData_Null() {
        System.out.println("testValidateDocumentMetaData_Null");
        ProvideAndRegisterDocumentSetRequestType body = null;
        XDRHelper instance = createHelper();

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSMissingDocument, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());

    }
    @Test
    public void testgetIntendedRecepients_Null() {
        System.out.println("testgetIntendedRecepients_Null");
        ProvideAndRegisterDocumentSetRequestType body = null;
        XDRHelper instance = createHelper();

        List<String> result = instance.getIntendedRecepients(body);

        assertNull(result);
    }
    @Test
    public void testgetIntendedRecepients_NullSubmitObj() {
        System.out.println("testgetIntendedRecepients_NullSubmitObj");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();
        XDRHelper instance = createHelper();

        body.setSubmitObjectsRequest(null);

        List<String> result = instance.getIntendedRecepients(body);

        assertNull(result);
    }
    @Ignore
    public void testgetIntendedRecepients_NoRecip() {
        System.out.println("testgetIntendedRecepients_NoRecip");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();
        XDRHelper instance = createHelper();

        List<String> result = instance.getIntendedRecepients(body);

        assertNotNull(result);

    }
    @Test
    public void testgetIntendedRecepients_Valid() {
        System.out.println("testgetIntendedRecepients_Valid");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage("ProvideAndRegisterDocumentSet-IntendedRecpient.xml");
        XDRHelper instance = createHelper();

        List<String> result = instance.getIntendedRecepients(body);

        assertNotNull(result);
        assertEquals(4, result.size());
    }
    @Ignore
    public void testValidateDocumentMetaData_ValidMessage() {
        System.out.println("testValidateDocumentMetaData_ValidMessage");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();
        XDRHelper instance = createHelper();


        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(0, result.getRegistryError().size());
        assertEquals("", result.getHighestSeverity());

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
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
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
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSMissingDocumentMetadata, result.getRegistryError().get(0).getErrorCode());
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
        assertEquals("", result.getHighestSeverity());
    }
    @Test
    public void testValidateDocumentMetaData_PatIdsNoMatch() {
        System.out.println("testValidateDocumentMetaData_PatIdsNoMatch");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage("ProvideAndRegisterDocumentSet-MultiPat.xml");


        XDRHelper instance = createHelper(true);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(XDRHelper.XDS_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
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