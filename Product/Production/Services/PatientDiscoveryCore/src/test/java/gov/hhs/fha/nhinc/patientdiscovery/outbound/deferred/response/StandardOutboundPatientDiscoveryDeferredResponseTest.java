/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.response.OutboundPatientDiscoveryDeferredResponseOrchestratable;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * @author akong
 * 
 */
public class StandardOutboundPatientDiscoveryDeferredResponseTest {

    private PRPAIN201306UV02 request;
    private AssertionType assertion;
    private NhinTargetCommunitiesType targets;
    private MCCIIN000002UV01 expectedResponse;
    private PRPAIN201306UV02 firstTargetRequest;
    private PRPAIN201306UV02 secondTargetRequest;
    
    @Before
    public void initialize() {
        request = new PRPAIN201306UV02();
        assertion = new AssertionType();
        targets = new NhinTargetCommunitiesType();
        expectedResponse = new MCCIIN000002UV01();
        firstTargetRequest = new PRPAIN201306UV02();
    }
    
    private List<UrlInfo> createUrlInfoList(String... hcids) {
        List<UrlInfo> urlInfoList = new ArrayList<UrlInfo>();
        
        for (int i = 0; i < hcids.length; i++) {
            urlInfoList.add(new UrlInfo());
            urlInfoList.get(i).setHcid(hcids[i]);
        }
        
        return urlInfoList;
    }
    
    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundPatientDiscoveryDeferredResponse> clazz = StandardOutboundPatientDiscoveryDeferredResponse.class;
        Method method = clazz.getMethod("processPatientDiscoveryAsyncResp", PRPAIN201306UV02.class,
                AssertionType.class, NhinTargetCommunitiesType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201306UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(MCCIIN000002UV01EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery Deferred Response", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void invoke() throws ConnectionManagerException {        
        List<UrlInfo> urlInfoList = createUrlInfoList("1.1", "2.2");

        // Mocks
        PatientDiscovery201306PolicyChecker policyChecker = mock(PatientDiscovery201306PolicyChecker.class);
        PatientDiscovery201306Processor pd201306Processor = mock(PatientDiscovery201306Processor.class);
        PatientDiscoveryAuditor auditLogger = mock(PatientDiscoveryAuditor.class);
        OutboundPatientDiscoveryDeferredResponseDelegate delegate = mock(OutboundPatientDiscoveryDeferredResponseDelegate.class);
        OutboundPatientDiscoveryDeferredResponseOrchestratable returnedOrchestratable = mock(OutboundPatientDiscoveryDeferredResponseOrchestratable.class);
        ConnectionManagerCache connectionManager = mock(ConnectionManagerCache.class);

        // Stubbing the methods
        when(connectionManager.getEndpointURLFromNhinTargetCommunities(targets, 
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME)).thenReturn(urlInfoList);

        when(pd201306Processor.createNewRequest(request, "1.1")).thenReturn(firstTargetRequest);
        when(pd201306Processor.createNewRequest(request, "2.2")).thenReturn(secondTargetRequest);

        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201306UV02RequestType.class))).thenReturn(
                true);

        when(delegate.process(any(OutboundPatientDiscoveryDeferredResponseOrchestratable.class))).thenReturn(
                returnedOrchestratable);

        when(returnedOrchestratable.getResponse()).thenReturn(expectedResponse);

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredResponse standardPatientDiscovery = new StandardOutboundPatientDiscoveryDeferredResponse(
                policyChecker, pd201306Processor, auditLogger, delegate, connectionManager);

        MCCIIN000002UV01 actualResponse = standardPatientDiscovery.processPatientDiscoveryAsyncResp(request, assertion,
                targets);

        // Verify actual response is the same as expected
        assertSame(expectedResponse, actualResponse);

        // Verify policy is checking the request containing the first target and then the second target
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> requestArgument = ArgumentCaptor
                .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);

        verify(policyChecker, times(2)).checkOutgoingPolicy(requestArgument.capture());
        assertEquals(firstTargetRequest, requestArgument.getAllValues().get(0).getPRPAIN201306UV02());
        assertEquals(secondTargetRequest, requestArgument.getAllValues().get(1).getPRPAIN201306UV02());
                
        // Verify the orchestratable is processing the request containing the first target and then the second target
        ArgumentCaptor<OutboundPatientDiscoveryDeferredResponseOrchestratable> orchestratableArgument = ArgumentCaptor
                .forClass(OutboundPatientDiscoveryDeferredResponseOrchestratable.class);

        verify(delegate, times(2)).process(orchestratableArgument.capture());
        assertEquals(firstTargetRequest, orchestratableArgument.getAllValues().get(0).getRequest());
        assertEquals(secondTargetRequest, orchestratableArgument.getAllValues().get(1).getRequest());
        
        // Verify request from adapter is audited
        requestArgument.getAllValues().clear();
        verify(auditLogger).auditEntityDeferred201306(requestArgument.capture(), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));
        assertEquals(request, requestArgument.getValue().getPRPAIN201306UV02());

        // Verify response to adapter is audited
        verify(auditLogger).auditAck(actualResponse, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
    }
    
    @Test
    public void noTargets() throws ConnectionManagerException {
        
        // Mocks
        ConnectionManagerCache connectionManager = mock(ConnectionManagerCache.class);
        PatientDiscoveryAuditor auditLogger = mock(PatientDiscoveryAuditor.class);

        // Stubbing the methods
        when(connectionManager.getEndpointURLFromNhinTargetCommunities(eq(targets), 
                eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME))).thenThrow(new ConnectionManagerException());

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredResponse standardPatientDiscovery = new StandardOutboundPatientDiscoveryDeferredResponse(
                null, null, auditLogger, null, connectionManager);

        MCCIIN000002UV01 errorResponse = standardPatientDiscovery.processPatientDiscoveryAsyncResp(request, assertion,
                targets);

        // Verify error response
        assertEquals(HL7AckTransforms.ACK_DETAIL_TYPE_CODE_ERROR, errorResponse.getAcknowledgement().get(0)
                .getAcknowledgementDetail().get(0).getTypeCode().toString());

        assertEquals("No Targets Found", errorResponse.getAcknowledgement().get(0).getAcknowledgementDetail().get(0)
                .getText().getContent().get(0).toString());

        // Verify request from adapter is audited
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> requestArgument = ArgumentCaptor
                .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);
        verify(auditLogger).auditEntityDeferred201306(requestArgument.capture(), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));
        assertEquals(request, requestArgument.getValue().getPRPAIN201306UV02());

        // Verify response to adapter is audited
        verify(auditLogger).auditAck(errorResponse, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
    }
    
    @Test
    public void policyFailed() throws ConnectionManagerException {
        List<UrlInfo> urlInfoList = createUrlInfoList("1.1");
        
        // Mocks
        PatientDiscovery201306PolicyChecker policyChecker = mock(PatientDiscovery201306PolicyChecker.class);
        PatientDiscovery201306Processor pd201306Processor = mock(PatientDiscovery201306Processor.class);
        PatientDiscoveryAuditor auditLogger = mock(PatientDiscoveryAuditor.class);
        OutboundPatientDiscoveryDeferredResponseDelegate delegate = mock(OutboundPatientDiscoveryDeferredResponseDelegate.class);
        ConnectionManagerCache connectionManager = mock(ConnectionManagerCache.class);

        // Stubbing the methods
        when(connectionManager.getEndpointURLFromNhinTargetCommunities(targets, 
                NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME)).thenReturn(urlInfoList);

        when(pd201306Processor.createNewRequest(request, "1.1")).thenReturn(firstTargetRequest);

        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201306UV02RequestType.class))).thenReturn(
                false);

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredResponse standardPatientDiscovery = new StandardOutboundPatientDiscoveryDeferredResponse(
                policyChecker, pd201306Processor, auditLogger, delegate, connectionManager);

        MCCIIN000002UV01 errorResponse = standardPatientDiscovery.processPatientDiscoveryAsyncResp(request, assertion,
                targets);
        
        // Verify error response
        assertEquals(HL7AckTransforms.ACK_DETAIL_TYPE_CODE_ERROR, errorResponse.getAcknowledgement().get(0)
                .getAcknowledgementDetail().get(0).getTypeCode().toString());

        assertEquals("Policy Check Failed", errorResponse.getAcknowledgement().get(0).getAcknowledgementDetail().get(0)
                .getText().getContent().get(0).toString());

        // Verify request from adapter is audited
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> requestArgument = ArgumentCaptor
                .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);
        verify(auditLogger).auditEntityDeferred201306(requestArgument.capture(), eq(assertion),
                eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION));
        assertEquals(request, requestArgument.getValue().getPRPAIN201306UV02());

        // Verify response to adapter is audited
        verify(auditLogger).auditAck(errorResponse, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_ENTITY_INTERFACE);
    }
}
