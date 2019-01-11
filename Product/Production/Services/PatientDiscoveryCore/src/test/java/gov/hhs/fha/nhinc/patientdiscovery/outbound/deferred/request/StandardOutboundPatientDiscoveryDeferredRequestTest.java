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
package gov.hhs.fha.nhinc.patientdiscovery.outbound.deferred.request;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.async.AsyncMessageProcessHelper;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.UrlInfo;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryPolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryDeferredRequestAuditTransforms;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.OutboundPatientDiscoveryDeferredRequestOrchestratable;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StandardOutboundPatientDiscoveryDeferredRequestTest {

    private PRPAIN201305UV02 request;
    private PRPAIN201305UV02 firstTargetRequest;
    private PRPAIN201305UV02 secondTargetRequest;
    private NhinTargetCommunitiesType targets;
    private MCCIIN000002UV01 expectedResponse;
    private II patientId;
    private AssertionType assertion;
    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final PatientDiscovery201305Processor pd201305Processor = mock(PatientDiscovery201305Processor.class);
    private final AsyncMessageProcessHelper asyncProcessHelper = mock(AsyncMessageProcessHelper.class);
    private final PatientDiscoveryPolicyChecker policyChecker = mock(PatientDiscoveryPolicyChecker.class);
    private final OutboundPatientDiscoveryDeferredRequestDelegate delegate = mock(
        OutboundPatientDiscoveryDeferredRequestDelegate.class);
    private final PDDeferredCorrelationDao correlationDao = mock(PDDeferredCorrelationDao.class);
    private final ExchangeManager exchangeManager = mock(ExchangeManager.class);
    private final OutboundPatientDiscoveryDeferredRequestOrchestratable orchestratableResponse
        = mock(OutboundPatientDiscoveryDeferredRequestOrchestratable.class);

    @Before
    public void initialize() {
        request = new PRPAIN201305UV02();
        firstTargetRequest = createPRPAIN201305UV02();
        secondTargetRequest = createPRPAIN201305UV02();
        assertion = new AssertionType();
        assertion.setMessageId("messageId");
        targets = new NhinTargetCommunitiesType();
        expectedResponse = new MCCIIN000002UV01();
        patientId = new II();
    }

    private PRPAIN201305UV02 createPRPAIN201305UV02() {
        PRPAIN201305UV02 newRequest = new PRPAIN201305UV02();
        newRequest.setControlActProcess(new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess());
        newRequest.getControlActProcess().setQueryByParameter(
            new JAXBElement<>(mock(QName.class),
                PRPAMT201306UV02QueryByParameter.class, new PRPAMT201306UV02QueryByParameter()));

        return newRequest;
    }

    private List<UrlInfo> createUrlInfoList(String... hcids) {
        List<UrlInfo> urlInfoList = new ArrayList<>();

        for (int i = 0; i < hcids.length; i++) {
            urlInfoList.add(new UrlInfo());
            urlInfoList.get(i).setHcid(hcids[i]);
        }

        return urlInfoList;
    }

    @Test
    public void hasOutboundProcessingEvent() throws Exception {
        Class<StandardOutboundPatientDiscoveryDeferredRequest> clazz
            = StandardOutboundPatientDiscoveryDeferredRequest.class;
        Method method = clazz.getMethod("processPatientDiscoveryAsyncReq", PRPAIN201305UV02.class, AssertionType.class,
            NhinTargetCommunitiesType.class);
        OutboundProcessingEvent annotation = method.getAnnotation(OutboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201305UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(MCCIIN000002UV01EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery Deferred Request", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void invoke() throws ExchangeManagerException {
        List<UrlInfo> urlInfoList = createUrlInfoList("1.1", "2.2");

        PatientDiscoveryDeferredRequestAuditLogger auditLogger = getAuditLogger(true);
        // Stubbing the methods
        when(
            exchangeManager.getEndpointURLFromNhinTargetCommunities(eq(targets),
                eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME))).thenReturn(urlInfoList);

        when(pd201305Processor.createNewRequest(request, "1.1")).thenReturn(firstTargetRequest);
        when(pd201305Processor.createNewRequest(request, "2.2")).thenReturn(secondTargetRequest);

        when(pd201305Processor.extractPatientIdFrom201305(request)).thenReturn(patientId);

        when(asyncProcessHelper.copyAssertionTypeObject(assertion)).thenReturn(assertion);

        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201305UV02RequestType.class))).thenReturn(
            true);

        when(orchestratableResponse.getResponse()).thenReturn(expectedResponse);

        when(delegate.process(any(OutboundPatientDiscoveryDeferredRequestOrchestratable.class))).thenReturn(
            orchestratableResponse);

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredRequest standardPatientDiscovery
            = new StandardOutboundPatientDiscoveryDeferredRequest(pd201305Processor, asyncProcessHelper, policyChecker,
                delegate, correlationDao, exchangeManager, auditLogger);

        MCCIIN000002UV01 actualResponse = standardPatientDiscovery.processPatientDiscoveryAsyncReq(request, assertion,
            targets);

        // Verify actual response is the same as expected
        assertSame(expectedResponse, actualResponse);

        // Verify correlations are stored
        ArgumentCaptor<RespondingGatewayPRPAIN201305UV02RequestType> requestArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201305UV02RequestType.class);
        verify(pd201305Processor).storeLocalMapping(requestArgument.capture());
        assertEquals(request, requestArgument.getValue().getPRPAIN201305UV02());

        verify(correlationDao).saveOrUpdate("messageId", patientId);
        // Verify policy is checking the request containing the first target and then the second target
        requestArgument.getAllValues().clear();
        verify(policyChecker, times(2)).checkOutgoingPolicy(requestArgument.capture());
        assertEquals(firstTargetRequest, requestArgument.getAllValues().get(0).getPRPAIN201305UV02());
        assertEquals(secondTargetRequest, requestArgument.getAllValues().get(1).getPRPAIN201305UV02());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        // Verify audits
        verify(mockEJBLogger, atLeast(1)).auditRequestMessage(eq(request), eq(assertion),
            any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME),
            any(PatientDiscoveryDeferredRequestAuditTransforms.class));
    }

    @Test
    public void noTargets() throws ExchangeManagerException {
        PatientDiscoveryDeferredRequestAuditLogger auditLogger = getAuditLogger(true);
        // Stubbing the methods
        when(
            exchangeManager.getEndpointURLFromNhinTargetCommunities(eq(targets),
                eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME))).thenThrow(
            new ExchangeManagerException());

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredRequest standardPatientDiscovery
            = new StandardOutboundPatientDiscoveryDeferredRequest(null, null, null, null, null, exchangeManager,
                auditLogger);

        MCCIIN000002UV01 errorResponse = standardPatientDiscovery.processPatientDiscoveryAsyncReq(request, assertion,
            targets);

        // Verify the error response contains expected values
        assertEquals(HL7AckTransforms.ACK_DETAIL_TYPE_CODE_ERROR, errorResponse.getAcknowledgement().get(0)
            .getAcknowledgementDetail().get(0).getTypeCode().toString());

        assertEquals(
            "No targets were found for the Patient Discovery Request",
            errorResponse.getAcknowledgement().get(0).getAcknowledgementDetail().get(0).getText().getContent()
                .get(0).toString());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        // Verify audits
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME),
            any(PatientDiscoveryDeferredRequestAuditTransforms.class));
    }

    @Test
    public void policyFailed() throws ExchangeManagerException {
        List<UrlInfo> urlInfoList = createUrlInfoList("1.1");
        PatientDiscoveryDeferredRequestAuditLogger auditLogger = getAuditLogger(true);
        // Stubbing the methods
        when(
            exchangeManager.getEndpointURLFromNhinTargetCommunities(eq(targets),
                eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME))).thenReturn(urlInfoList);

        when(pd201305Processor.createNewRequest(eq(request), any(String.class))).thenReturn(firstTargetRequest);

        when(pd201305Processor.extractPatientIdFrom201305(request)).thenReturn(patientId);

        when(asyncProcessHelper.copyAssertionTypeObject(assertion)).thenReturn(assertion);

        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201305UV02RequestType.class))).thenReturn(
            false);

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredRequest standardPatientDiscovery
            = new StandardOutboundPatientDiscoveryDeferredRequest(pd201305Processor, asyncProcessHelper, policyChecker,
                delegate, correlationDao, exchangeManager, auditLogger);

        MCCIIN000002UV01 errorResponse = standardPatientDiscovery.processPatientDiscoveryAsyncReq(request, assertion,
            targets);

        // Verify the error response contains expected values
        assertEquals(HL7AckTransforms.ACK_DETAIL_TYPE_CODE_ERROR, errorResponse.getAcknowledgement().get(0)
            .getAcknowledgementDetail().get(0).getTypeCode().toString());

        assertEquals("Policy Check Failed", errorResponse.getAcknowledgement().get(0).getAcknowledgementDetail().get(0)
            .getText().getContent().get(0).toString());

        // Verify correlations are stored
        ArgumentCaptor<RespondingGatewayPRPAIN201305UV02RequestType> requestArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201305UV02RequestType.class);
        verify(pd201305Processor).storeLocalMapping(requestArgument.capture());
        assertEquals(request, requestArgument.getValue().getPRPAIN201305UV02());

        verify(correlationDao).saveOrUpdate("messageId", patientId);
        // Verify policy check
        requestArgument.getAllValues().clear();
        verify(policyChecker).checkOutgoingPolicy(requestArgument.capture());
        assertEquals(firstTargetRequest, requestArgument.getValue().getPRPAIN201305UV02());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME),
            any(PatientDiscoveryDeferredRequestAuditTransforms.class));
    }

    @Test
    public void auditOffForOutboundPDDeferredReq() throws ExchangeManagerException {
        List<UrlInfo> urlInfoList = createUrlInfoList("1.1", "2.2");

        PatientDiscoveryDeferredRequestAuditLogger auditLogger = getAuditLogger(false);
        // Stubbing the methods
        when(
            exchangeManager.getEndpointURLFromNhinTargetCommunities(eq(targets),
                eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME))).thenReturn(urlInfoList);

        when(pd201305Processor.createNewRequest(request, "1.1")).thenReturn(firstTargetRequest);
        when(pd201305Processor.createNewRequest(request, "2.2")).thenReturn(secondTargetRequest);

        when(pd201305Processor.extractPatientIdFrom201305(request)).thenReturn(patientId);

        when(asyncProcessHelper.copyAssertionTypeObject(assertion)).thenReturn(assertion);

        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201305UV02RequestType.class))).thenReturn(
            true);

        when(orchestratableResponse.getResponse()).thenReturn(expectedResponse);

        when(delegate.process(any(OutboundPatientDiscoveryDeferredRequestOrchestratable.class))).thenReturn(
            orchestratableResponse);

        // Actual invocation
        StandardOutboundPatientDiscoveryDeferredRequest standardPatientDiscovery
            = new StandardOutboundPatientDiscoveryDeferredRequest(pd201305Processor, asyncProcessHelper, policyChecker,
                delegate, correlationDao, exchangeManager, auditLogger);

        MCCIIN000002UV01 actualResponse = standardPatientDiscovery.processPatientDiscoveryAsyncReq(request, assertion,
            targets);

        // Verify actual response is the same as expected
        assertSame(expectedResponse, actualResponse);

        // Verify correlations are stored
        ArgumentCaptor<RespondingGatewayPRPAIN201305UV02RequestType> requestArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201305UV02RequestType.class);
        verify(pd201305Processor).storeLocalMapping(requestArgument.capture());
        assertEquals(request, requestArgument.getValue().getPRPAIN201305UV02());

        verify(correlationDao).saveOrUpdate("messageId", patientId);
        // Verify policy is checking the request containing the first target and then the second target
        requestArgument.getAllValues().clear();
        verify(policyChecker, times(2)).checkOutgoingPolicy(requestArgument.capture());
        assertEquals(firstTargetRequest, requestArgument.getAllValues().get(0).getPRPAIN201305UV02());
        assertEquals(secondTargetRequest, requestArgument.getAllValues().get(1).getPRPAIN201305UV02());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        // Verify audits
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request), eq(assertion), any(NhinTargetSystemType.class),
            eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION), eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE),
            eq(Boolean.TRUE), isNull(Properties.class), eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME),
            any(PatientDiscoveryDeferredRequestAuditTransforms.class));
    }

    private HomeCommunityType getHomeCommunity(String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        return homeCommunity;
    }

    private PatientDiscoveryDeferredRequestAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new PatientDiscoveryDeferredRequestAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isLoggingOn;
            }
        };
    }

}
