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
package gov.hhs.fha.nhinc.docsubmission.adapter.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


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
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSMissingDocument, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());

    }

    @Test
    public void testgetIntendedRecepients_Null() {
        System.out.println("testgetIntendedRecepients_Null");
        ProvideAndRegisterDocumentSetRequestType body = null;
        XDRHelper instance = createHelper();

        List<String> result = instance.getIntendedRecepients(body);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testgetIntendedRecepients_NullSubmitObj() {
        System.out.println("testgetIntendedRecepients_NullSubmitObj");
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper().getSampleMessage();
        XDRHelper instance = createHelper();

        body.setSubmitObjectsRequest(null);

        List<String> result = instance.getIntendedRecepients(body);

        assertTrue(result.isEmpty());
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
        ProvideAndRegisterDocumentSetRequestType body = new XDRMessageHelper()
            .getSampleMessage("ProvideAndRegisterDocumentSet-IntendedRecpient.xml");
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
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSMissingDocument, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());

    }

    @Test
    public void testValidateDocumentMetaData_UnsupportedMimeType() {
        System.out.println("testValidateDocumentMetaData_UnsupportedMimeType");
        ProvideAndRegisterDocumentSetRequestType body = XDRMessageHelper.getSampleMessage();

        XDRHelper instance = createHelper(false);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());
    }

    @Test
    public void testValidateDocumentMetaData_SupportedMimeType() {
        System.out.println("testValidateDocumentMetaData_SupportedMimeType");
        ProvideAndRegisterDocumentSetRequestType body = XDRMessageHelper.getSampleMessage();

        XDRHelper instance = createHelper(true);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
    }

    @Test
    public void testValidateDocumentMetaData_PatIdsNoMatch() {
        System.out.println("testValidateDocumentMetaData_PatIdsNoMatch");
        ProvideAndRegisterDocumentSetRequestType body = XDRMessageHelper
            .getSampleMessage("ProvideAndRegisterDocumentSet-MultiPat.xml");

        XDRHelper instance = createHelper(true);

        RegistryErrorList result = instance.validateDocumentMetaData(body);
        assertNotNull(result);
        assertEquals(1, result.getRegistryError().size());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getHighestSeverity());
        assertEquals(NhincConstants.XDS_REGISTRY_ERROR_SEVERITY_ERROR, result.getRegistryError().get(0).getSeverity());
        assertEquals(XDRHelper.XDR_EC_XDSPatientIdDoesNotMatch, result.getRegistryError().get(0).getErrorCode());
        assertNotNull(result.getRegistryError().get(0).getCodeContext());
    }

    private XDRHelper createHelper() {
        // TestHelper helper = new TestHelper();

        XDRHelper result = new XDRHelper() {

            @Override
            protected boolean checkIdsMatch() {
                return true;
            }
        };
        return result;
    }

    private XDRHelper createHelper(boolean supportedMimeType) {
        final boolean boolResult = supportedMimeType;
        // TestHelper helper = new TestHelper();

        XDRHelper result = new XDRHelper() {

            @Override
            protected boolean isSupportedMimeType(String mimeType) {
                return boolResult;
            }

            @Override
            protected boolean checkIdsMatch() {
                return true;
            }

        };
        return result;
    }

}
