/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.nhinc.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
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
public class NhincDocQueryDeferredRequestTest {

    private Mockery mockery = null;

    public NhincDocQueryDeferredRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        mockery = new Mockery() {
            {
                setImposteriser(ClassImposteriser.INSTANCE);
            }
        };
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of crossGatewayQueryRequest method, of class NhincDocQueryDeferredRequest.
     */
    @Test
    public void testCrossGatewayQueryRequestHappyPath() {
        System.out.println("crossGatewayQueryRequest -- Happy Path");

        final Log mockLogger = mockery.mock(Log.class);
        final NhincDocQueryDeferredRequestOrchImpl mockNhincDocQueryDeferredRequestOrchImpl = mockery.mock(NhincDocQueryDeferredRequestOrchImpl.class);

        RespondingGatewayCrossGatewayQueryRequestType crossGatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();

        NhincDocQueryDeferredRequest instance = new NhincDocQueryDeferredRequest(){
            protected Log getLogger(){
                return mockLogger;
            }

            @Override
            protected NhincDocQueryDeferredRequestOrchImpl getNhincDocQueryDeferredRequestOrchImpl() {
                return mockNhincDocQueryDeferredRequestOrchImpl;
            }
        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).isDebugEnabled();
                will(returnValue(true));
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockNhincDocQueryDeferredRequestOrchImpl).crossGatewayQueryRequest(with(any(RespondingGatewayCrossGatewayQueryRequestType.class)),
                        with(any(WebServiceContext.class)));
                will(returnValue(new DocQueryAcknowledgementType()));
            }
        });

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(crossGatewayQueryRequest);
        assertNotNull(result);
    }

}