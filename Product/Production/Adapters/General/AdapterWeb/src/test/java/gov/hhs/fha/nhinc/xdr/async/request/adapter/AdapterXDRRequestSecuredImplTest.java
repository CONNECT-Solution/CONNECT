/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.xdr.async.request.adapter;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.nhincentityxdrsecured.async.response.EntityXDRSecuredAsyncResponsePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
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
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author patlollav
 */
public class AdapterXDRRequestSecuredImplTest {

    public AdapterXDRRequestSecuredImplTest() {
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
     * Test of provideAndRegisterDocumentSetBRequest method, of class AdapterXDRRequestSecuredImpl.
     */
    @Ignore
    public void testProvideAndRegisterDocumentSetBRequest() {
        System.out.println("provideAndRegisterDocumentSetBRequest");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
        final Log mockLogger = mockery.mock(Log.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final WebServiceContext context = mockery.mock(WebServiceContext.class);

        AdapterXDRRequestSecuredImpl adapterXDRRequestSecuredImpl = new AdapterXDRRequestSecuredImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected AssertionType createAssertion(WebServiceContext context) {
                return mockAssertion;
            }

            @Override
            protected RegistryResponseType callAdapterComponentXDR(ProvideAndRegisterDocumentSetRequestType body, AssertionType assertion) {
                return new RegistryResponseType();
            }

            @Override
            public XDRAcknowledgementType sendXDRResponse(RegistryResponseType body, AssertionType assertion) {
                XDRAcknowledgementType ack = new XDRAcknowledgementType();
                RegistryResponseType regResp = new RegistryResponseType();
                regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
                ack.setMessage(regResp);
                return ack;
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        AdapterProvideAndRegisterDocumentSetSecuredRequestType body = new AdapterProvideAndRegisterDocumentSetSecuredRequestType();
        ProvideAndRegisterDocumentSetRequestType iheMsg = new ProvideAndRegisterDocumentSetRequestType();
        body.setProvideAndRegisterDocumentSetRequest(iheMsg);
        XDRAcknowledgementType result = adapterXDRRequestSecuredImpl.provideAndRegisterDocumentSetBRequest(body, context);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());


    }

    /**
     * Test of sendXDRResponse method, of class AdapterXDRRequestSecuredImpl.
     */
    @Test
    public void testSendXDRResponseWithValidURL() {
        System.out.println("sendXDRResponseWithValidURL");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        AdapterXDRRequestSecuredImpl adapterXDRRequestSecuredImpl = new AdapterXDRRequestSecuredImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String getEntityXDRSecuredResponseEndPointURL() {
                return "URL";
            }

            @Override
            protected EntityXDRSecuredAsyncResponsePortType getEntityXDRSecuredResponsePort(String url) {
                EntityXDRSecuredAsyncResponsePortType mockPort = new EntityXDRSecuredAsyncResponsePortType() {

                    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType arg0) {
                        return ack;
                    }
                };
                return mockPort;

            }

            @Override
            protected void setRequestContext(AssertionType assertion, String adapterXDRRequestSecuredEndPointURL, EntityXDRSecuredAsyncResponsePortType port) {
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
            }
        });

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();

        XDRAcknowledgementType result = adapterXDRRequestSecuredImpl.sendXDRResponse(body, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());

    }

    /**
     * Test of sendXDRResponse method, of class AdapterXDRRequestSecuredImpl.
     */
    @Test
    public void testSendXDRResponseWithInvalidURL() {
        System.out.println("sendXDRResponseWithInvalidURL");
        Mockery mockery = new Mockery() {

            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };

        final Log mockLogger = mockery.mock(Log.class);
        final XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        AdapterXDRRequestSecuredImpl adapterXDRRequestSecuredImpl = new AdapterXDRRequestSecuredImpl() {

            @Override
            protected Log getLogger() {
                return mockLogger;
            }

            @Override
            protected String getEntityXDRSecuredResponseEndPointURL() {
                return null;
            }

            @Override
            protected EntityXDRSecuredAsyncResponsePortType getEntityXDRSecuredResponsePort(String url) {
                EntityXDRSecuredAsyncResponsePortType mockPort = new EntityXDRSecuredAsyncResponsePortType() {

                    public XDRAcknowledgementType provideAndRegisterDocumentSetBAsyncResponse(RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType arg0) {
                        return ack;
                    }
                };
                return mockPort;

            }

            @Override
            protected void setRequestContext(AssertionType assertion, String adapterXDRRequestSecuredEndPointURL, EntityXDRSecuredAsyncResponsePortType port) {
            }
        };

        mockery.checking(new Expectations() {

            {
                allowing(mockLogger).debug(with(any(String.class)));
                allowing(mockLogger).error(with(any(String.class)));
            }
        });

        RegistryResponseType body = new RegistryResponseType();
        AssertionType assertion = new AssertionType();

        XDRAcknowledgementType result = adapterXDRRequestSecuredImpl.sendXDRResponse(body, assertion);
        assertEquals(NhincConstants.XDR_ACK_STATUS_MSG, result.getMessage().getStatus());

    }
}
