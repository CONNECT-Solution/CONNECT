/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.gateway.executorservice;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author nsubramanyan
 */
public class NhinCallableRequestTest {

    public NhinCallableRequestTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of call method, of class NhinCallableRequest.
     */
    @Test
    public void testCall() {
        System.out.println("call");
        TestOutboundOrchestratableMessage test = new TestOutboundOrchestratableMessage();
        NhinCallableRequest<OutboundOrchestratableMessage> instance = new NhinCallableRequest<OutboundOrchestratableMessage>(test);
        Object result = instance.call();
        assertNotNull(result);
    }

    /**
     * Test of call method, of class NhinCallableRequest.
     */
    @Test(expected = Exception.class)
    public void testCallWithNullResponse() {
        System.out.println("call");
        TestOutboundOrchestratableMessage test = new TestOutboundOrchestratableMessage();
        test.setReturnNullObject(true);
        NhinCallableRequest<OutboundOrchestratableMessage> instance = new NhinCallableRequest<OutboundOrchestratableMessage>(test);
        Object result = instance.call();
    }

    /**
     * Test of call method, of class NhinCallableRequest.
     */
    @Test(expected = Exception.class)
    public void testCallWithNullClient() {
        System.out.println("call");
        TestOutboundOrchestratableMessage test = new TestOutboundOrchestratableMessage();
        test.setReturnNullOutboundDelegate(true);
        NhinCallableRequest<OutboundOrchestratableMessage> instance = new NhinCallableRequest<OutboundOrchestratableMessage>(test);
        Object result = instance.call();
    }

    private static class TestOutboundOrchestratableMessage implements OutboundOrchestratableMessage {

        private boolean returnNullObject = false;
        private boolean returnNullOutboundDelegate = false;

        @Override
        public Optional<OutboundResponseProcessor> getResponseProcessor() {
            return Optional.of((OutboundResponseProcessor) new TestOutboundResponse());
        }

        @Override
        public OutboundDelegate getDelegate() {
            if (returnNullOutboundDelegate) {
                return null;
            } else {
                return new TestOutboundDelegate(returnNullObject);
            }
        }

        @Override
        public NhinAggregator getAggregator() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isPassthru() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public AuditTransformer getAuditTransformer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public PolicyTransformer getPolicyTransformer() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public AssertionType getAssertion() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getServiceName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * @param returnNullObject the returnNullObject to set
         */
        public void setReturnNullObject(boolean returnNullObject) {
            this.returnNullObject = returnNullObject;
        }

        /**
         * @param returnNullOutboundDelegate the returnNullOutboundDelegate to set
         */
        public void setReturnNullOutboundDelegate(boolean returnNullOutboundDelegate) {
            this.returnNullOutboundDelegate = returnNullOutboundDelegate;
        }
    }

    private static class TestOutboundDelegate implements OutboundDelegate {

        private boolean returnNullObject = false;

        public TestOutboundDelegate(boolean value) {
            this.returnNullObject = value;
        }

        @Override
        public OutboundOrchestratable process(OutboundOrchestratable message) {
            if (returnNullObject) {
                return null;
            } else {
                return new TestOutboundOrchestratableMessage();
            }
        }

        @Override
        public void createErrorResponse(OutboundOrchestratable message, String error) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Orchestratable process(Orchestratable message) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        /**
         * @param returnNullObject the returnNullObject to set
         */
        public void setReturnNullObject(boolean returnNullObject) {
            this.returnNullObject = returnNullObject;
        }
    }

    private static class TestOutboundResponse implements gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor {

        @Override
        public OutboundOrchestratableMessage processNhinResponse(OutboundOrchestratableMessage individualResponse, OutboundOrchestratableMessage cumulativeResponse) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public OutboundOrchestratableMessage processErrorResponse(OutboundOrchestratableMessage request, String error) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void aggregate(OutboundOrchestratable to, OutboundOrchestratable from) {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
