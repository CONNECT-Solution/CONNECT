/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.gateway.executorservice;

import com.google.common.base.Optional;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.orchestration.AuditTransformer;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundDelegate;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratableMessage;
import gov.hhs.fha.nhinc.orchestration.OutboundResponseProcessor;
import gov.hhs.fha.nhinc.orchestration.PolicyTransformer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author nsubramanyan
 */
public class NhinTaskExecutorTest {

    public NhinTaskExecutorTest() {
    }

    /**
     * Test of getFinalResponse method, of class NhinTaskExecutor.
     */
    @Test
    public void testGetFinalResponse() {
        System.out.println("getFinalResponse");
        NhinTaskExecutor<OutboundOrchestratableMessage, OutboundOrchestratableMessage> instance = new NhinTaskExecutor<OutboundOrchestratableMessage, OutboundOrchestratableMessage>(null, null, null);
        Object expResult = null;
        Object result = instance.getFinalResponse();
        assertEquals(expResult, result);
    }

    /**
     * Test of executeTask method, of class NhinTaskExecutor.
     */
    @Test
    public void testExecuteTask() throws Exception {
        System.out.println("executeTask");
        String transactionId = (UUID.randomUUID()).toString();
        List<NhinCallableRequest<TestOutboundOrchestratableMessage>> callableList = new ArrayList<NhinCallableRequest<TestOutboundOrchestratableMessage>>();
        // loop through the communities and send request if results were not null
        for (UrlInfo urlInfo : getUrlList()) {
            TestOutboundOrchestratableMessage message = new TestOutboundOrchestratableMessage();
            callableList.add(new NhinCallableRequest<TestOutboundOrchestratableMessage>(message));
        }
        NhinTaskExecutor<TestOutboundOrchestratableMessage, TestOutboundOrchestratableMessage> instance = new NhinTaskExecutor<TestOutboundOrchestratableMessage, TestOutboundOrchestratableMessage>(Executors.newFixedThreadPool(1), callableList, transactionId);
        instance.executeTask();
        Object result = instance.getFinalResponse();
        assertNotNull(result);
    }

    @Test(expected = Exception.class)
    public void testExecuteTaskWithException() throws Exception {
        System.out.println("executeTask");
        String transactionId = (UUID.randomUUID()).toString();
        List<NhinCallableRequest<TestOutboundOrchestratableMessage>> callableList = new ArrayList<NhinCallableRequest<TestOutboundOrchestratableMessage>>();
        // loop through the communities and send request if results were not null
        for (UrlInfo urlInfo : getUrlList()) {
            TestOutboundOrchestratableMessage message = new TestOutboundOrchestratableMessage();
            message.setReturnNullOutboundResponse(true);
            callableList.add(new NhinCallableRequest<TestOutboundOrchestratableMessage>(message));
        }
        NhinTaskExecutor<TestOutboundOrchestratableMessage, TestOutboundOrchestratableMessage> instance = new NhinTaskExecutor<TestOutboundOrchestratableMessage, TestOutboundOrchestratableMessage>(Executors.newFixedThreadPool(1), callableList, transactionId);
        instance.executeTask();
        Object result = instance.getFinalResponse();
        assertNotNull(result);
    }

    private static class TestOutboundOrchestratableMessage implements OutboundOrchestratableMessage {

        private boolean returnNullObject = false;
        private boolean returnNullOutboundResponse = false;

        @Override
        public Optional<OutboundResponseProcessor> getResponseProcessor() {

            if (returnNullOutboundResponse) {
                return Optional.absent();
            } else {
                return Optional.of((OutboundResponseProcessor) new TestOutboundResponse());
            }
        }

        @Override
        public OutboundDelegate getDelegate() {
            return new TestOutboundDelegate(returnNullObject);
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
         * @param returnNullOutboundResponse the returnNullOutboundResponse to set
         */
        public void setReturnNullOutboundResponse(boolean returnNullOutboundResponse) {
            this.returnNullOutboundResponse = returnNullOutboundResponse;
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
            return individualResponse;
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

    private List<UrlInfo> getUrlList() {
        Set<UrlInfo> endpointUrlSet = new HashSet<UrlInfo>();
        UrlInfo entry = new UrlInfo();
        entry.setHcid("1.1");
        entry.setUrl("TestURL2");
        endpointUrlSet.add(entry);
        entry = new UrlInfo();
        entry.setHcid("2.2");
        entry.setUrl("TestURL2");
        endpointUrlSet.add(entry);
        List<UrlInfo> endpointUrlList = new ArrayList<UrlInfo>(endpointUrlSet);
        return endpointUrlList;

    }
}
