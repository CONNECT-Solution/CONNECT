/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.xdr.async.response.adapter;

import ihe.iti.xdr._2007.AcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author patlollav
 */
public class AdapterXDRResponseSecuredImplTest {

    public AdapterXDRResponseSecuredImplTest() {
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

    /**
     * Test of provideAndRegisterDocumentSetBResponse method, of class AdapterXDRResponseSecuredImpl.
     */
    @Test
    public void testProvideAndRegisterDocumentSetBResponse() {
/*
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final WebServiceContext context = mockery.mock(WebServiceContext.class);
        
        AdapterXDRResponseSecuredImpl adapterXDRResponseSecuredImpl = new AdapterXDRResponseSecuredImpl(){


            @Override
            protected Log getLogger() {
                return mockLogger;
            }
        };
        
        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        RegistryResponseType request = new RegistryResponseType();
        AcknowledgementType result = adapterXDRResponseSecuredImpl.provideAndRegisterDocumentSetBResponse(request, context);
        assertEquals("SUCCESS", result.getMessage()); 
             */
    }
}