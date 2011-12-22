/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
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
public class PassthruDocQueryDeferredRequestSecuredImplTest {

    private Mockery mockery = null;
    
    public PassthruDocQueryDeferredRequestSecuredImplTest() {
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
     * Test of crossGatewayQueryRequest method, of class PassthruDocQueryDeferredRequestSecured.
     */
    @Test
    public void testCrossGatewayQueryRequest() {
        System.out.println("crossGatewayQueryRequest -- Happy Path");

        final Log mockLogger = mockery.mock(Log.class);
        final PassthruDocQueryDeferredRequestOrchImpl mockNhincDocQueryDeferredRequestOrchImpl = mockery.mock(PassthruDocQueryDeferredRequestOrchImpl.class);

        final RespondingGatewayCrossGatewayQuerySecuredRequestType mockCrossGatewayQueryRequest = mockery.mock(RespondingGatewayCrossGatewayQuerySecuredRequestType.class);
        final AssertionType mockAssertion = mockery.mock(AssertionType.class);
        final WebServiceContext mockContext = mockery.mock(WebServiceContext.class);


        PassthruDocQueryDeferredRequestSecuredImpl instance = new PassthruDocQueryDeferredRequestSecuredImpl(){

            protected Log getLogger(){
                return mockLogger;
            }

            @Override
            protected PassthruDocQueryDeferredRequestOrchImpl getPassthruDocQueryDeferredRequestOrchImpl() {
                return mockNhincDocQueryDeferredRequestOrchImpl;
            }

            @Override
            protected AssertionType extractAssertion(WebServiceContext context) {
                return mockAssertion;
            }


        };

        mockery.checking(new Expectations() {
            {
                allowing(mockLogger).debug(with(any(String.class)));
                one(mockAssertion).setMessageId(with(any(String.class)));
                one(mockCrossGatewayQueryRequest).getAdhocQueryRequest();
                one(mockCrossGatewayQueryRequest).getNhinTargetSystem();
                one(mockNhincDocQueryDeferredRequestOrchImpl).crossGatewayQueryRequest(with(any(AdhocQueryRequest.class)), with(any(AssertionType.class)),
                        with(any(NhinTargetSystemType.class)));
                will(returnValue(new DocQueryAcknowledgementType()));

            }
        });

        DocQueryAcknowledgementType result = instance.crossGatewayQueryRequest(mockCrossGatewayQueryRequest, null);
        assertNotNull(result);
    }

 
}