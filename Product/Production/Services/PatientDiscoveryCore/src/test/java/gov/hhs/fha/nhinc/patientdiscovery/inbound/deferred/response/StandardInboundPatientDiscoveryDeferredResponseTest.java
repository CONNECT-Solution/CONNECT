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
package gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.response;

import gov.hhs.fha.nhinc.aspect.InboundProcessingEvent;
import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientcorrelation.nhinc.dao.PDDeferredCorrelationDao;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306PolicyChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201306Processor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.MCCIIN000002UV01EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201306UV02EventDescriptionBuilder;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredResponseAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryDeferredResponseAuditTransforms;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseFactory.ResponseModeType;
import gov.hhs.fha.nhinc.patientdiscovery.response.ResponseMode;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import java.lang.reflect.Method;
import java.util.Properties;
import org.hl7.v3.II;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02RequestType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author achidamb
 *
 */
public class StandardInboundPatientDiscoveryDeferredResponseTest {

    private final PRPAIN201306UV02 request = new PRPAIN201306UV02();
    private final AssertionType assertion = new AssertionType();
    private final Properties webContextProperties = new Properties();
    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);

    @Test
    public void hasInboundProcessingEvent() throws Exception {
        Class<StandardInboundPatientDiscoveryDeferredResponse> clazz
            = StandardInboundPatientDiscoveryDeferredResponse.class;
        Method method = clazz.getDeclaredMethod("respondingGatewayDeferredPRPAIN201306UV02", PRPAIN201306UV02.class,
            AssertionType.class, Properties.class);
        InboundProcessingEvent annotation = method.getAnnotation(InboundProcessingEvent.class);
        assertNotNull(annotation);
        assertEquals(PRPAIN201306UV02EventDescriptionBuilder.class, annotation.beforeBuilder());
        assertEquals(MCCIIN000002UV01EventDescriptionBuilder.class, annotation.afterReturningBuilder());
        assertEquals("Patient Discovery Deferred Response", annotation.serviceType());
        assertEquals("1.0", annotation.version());
    }

    @Test
    public void invoke() {
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();
        II patientId = new II();

        // Mocks
        PatientDiscovery201306PolicyChecker policyChecker = mock(PatientDiscovery201306PolicyChecker.class);
        ResponseFactory responseFactory = mock(ResponseFactory.class);
        PatientDiscovery201306Processor msgProcessor = mock(PatientDiscovery201306Processor.class);
        PDDeferredCorrelationDao pdCorrelationDao = mock(PDDeferredCorrelationDao.class);
        ResponseMode responseMode = mock(ResponseMode.class);
        AdapterPatientDiscoveryDeferredRespProxyObjectFactory adapterProxyFactory
            = mock(AdapterPatientDiscoveryDeferredRespProxyObjectFactory.class);
        AdapterPatientDiscoveryDeferredRespProxy adapterProxy = mock(AdapterPatientDiscoveryDeferredRespProxy.class);
        PatientDiscoveryDeferredResponseAuditLogger auditLogger = getAuditLogger(true);
        // Stubbing the methods
        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201306UV02RequestType.class))).thenReturn(
            true);

        when(responseFactory.getResponseModeType()).thenReturn(ResponseModeType.VERIFY);

        when(responseFactory.getResponseMode()).thenReturn(responseMode);

        when(pdCorrelationDao.queryByMessageId(any(String.class))).thenReturn(patientId);

        when(adapterProxyFactory.create()).thenReturn(adapterProxy);
        when(adapterProxy.processPatientDiscoveryAsyncResp(request, assertion)).thenReturn(expectedResponse);

        // Actual invocation
        StandardInboundPatientDiscoveryDeferredResponse standardPatientDiscovery
            = new StandardInboundPatientDiscoveryDeferredResponse(
                policyChecker, responseFactory, msgProcessor, adapterProxyFactory, pdCorrelationDao, auditLogger);

        MCCIIN000002UV01 actualResponse = standardPatientDiscovery.respondingGatewayDeferredPRPAIN201306UV02(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        // Verify policy check is with the correct request
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> policyReqArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);

        verify(policyChecker).checkOutgoingPolicy(policyReqArgument.capture());
        assertEquals(request, policyReqArgument.getValue().getPRPAIN201306UV02());

        // Verify response mode is called
        verify(responseMode).processResponse(request, assertion, patientId);

        // Verify audits
        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME),
            any(PatientDiscoveryDeferredResponseAuditTransforms.class));
    }

    @Test
    public void passthrough() {
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        // Mocks
        PatientDiscovery201306PolicyChecker policyChecker = mock(PatientDiscovery201306PolicyChecker.class);
        ResponseFactory responseFactory = mock(ResponseFactory.class);
        ResponseMode responseMode = mock(ResponseMode.class);
        AdapterPatientDiscoveryDeferredRespProxyObjectFactory adapterProxyFactory
            = mock(AdapterPatientDiscoveryDeferredRespProxyObjectFactory.class);
        AdapterPatientDiscoveryDeferredRespProxy adapterProxy = mock(AdapterPatientDiscoveryDeferredRespProxy.class);
        PatientDiscoveryDeferredResponseAuditLogger auditLogger = getAuditLogger(true);
        // Stubbing the methods
        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201306UV02RequestType.class))).thenReturn(
            true);

        when(responseFactory.getResponseModeType()).thenReturn(ResponseModeType.PASSTHROUGH);

        when(adapterProxyFactory.create()).thenReturn(adapterProxy);
        when(adapterProxy.processPatientDiscoveryAsyncResp(request, assertion)).thenReturn(expectedResponse);

        // Actual invocation
        StandardInboundPatientDiscoveryDeferredResponse standardPatientDiscovery
            = new StandardInboundPatientDiscoveryDeferredResponse(policyChecker, responseFactory, null,
                adapterProxyFactory, null, auditLogger);

        MCCIIN000002UV01 actualResponse = standardPatientDiscovery.respondingGatewayDeferredPRPAIN201306UV02(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        // Verify policy check is with the correct request
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> policyReqArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);

        verify(policyChecker).checkOutgoingPolicy(policyReqArgument.capture());
        assertEquals(request, policyReqArgument.getValue().getPRPAIN201306UV02());

        // Verify response mode processing is never called
        verify(responseMode, never()).processResponse(any(PRPAIN201306UV02.class), any(AssertionType.class),
            any(II.class));

        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME),
            any(PatientDiscoveryDeferredResponseAuditTransforms.class));
    }

    @Test
    public void policyFailed() {
        // Mocks
        PatientDiscovery201306PolicyChecker policyChecker = mock(PatientDiscovery201306PolicyChecker.class);
        PatientDiscoveryDeferredResponseAuditLogger auditLogger = getAuditLogger(true);
        // Stubbing the methods
        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201306UV02RequestType.class))).thenReturn(
            false);

        // Actual invocation
        StandardInboundPatientDiscoveryDeferredResponse standardPatientDiscovery = new StandardInboundPatientDiscoveryDeferredResponse(policyChecker, null, null, null, null, auditLogger);

        MCCIIN000002UV01 errorResponse = standardPatientDiscovery.respondingGatewayDeferredPRPAIN201306UV02(request,
            assertion, webContextProperties);

        // Verify error response
        assertEquals(HL7AckTransforms.ACK_DETAIL_TYPE_CODE_ERROR, errorResponse.getAcknowledgement().get(0)
            .getAcknowledgementDetail().get(0).getTypeCode().toString());

        assertEquals("Policy Check Failed", errorResponse.getAcknowledgement().get(0).getAcknowledgementDetail().get(0)
            .getText().getContent().get(0).toString());

        // Verify policy check is with the correct request
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> policyReqArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);

        verify(policyChecker).checkOutgoingPolicy(policyReqArgument.capture());
        assertEquals(request, policyReqArgument.getValue().getPRPAIN201306UV02());

        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(errorResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME),
            any(PatientDiscoveryDeferredResponseAuditTransforms.class));
    }

    @Test
    public void auditOffForInboundPDDeferredResp() {
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();
        II patientId = new II();

        // Mocks
        PatientDiscovery201306PolicyChecker policyChecker = mock(PatientDiscovery201306PolicyChecker.class);
        ResponseFactory responseFactory = mock(ResponseFactory.class);
        PatientDiscovery201306Processor msgProcessor = mock(PatientDiscovery201306Processor.class);
        PDDeferredCorrelationDao pdCorrelationDao = mock(PDDeferredCorrelationDao.class);
        ResponseMode responseMode = mock(ResponseMode.class);
        AdapterPatientDiscoveryDeferredRespProxyObjectFactory adapterProxyFactory
            = mock(AdapterPatientDiscoveryDeferredRespProxyObjectFactory.class);
        AdapterPatientDiscoveryDeferredRespProxy adapterProxy = mock(AdapterPatientDiscoveryDeferredRespProxy.class);
        PatientDiscoveryDeferredResponseAuditLogger auditLogger = getAuditLogger(false);
        // Stubbing the methods
        when(policyChecker.checkOutgoingPolicy(any(RespondingGatewayPRPAIN201306UV02RequestType.class))).thenReturn(
            true);

        when(responseFactory.getResponseModeType()).thenReturn(ResponseModeType.VERIFY);

        when(responseFactory.getResponseMode()).thenReturn(responseMode);

        when(pdCorrelationDao.queryByMessageId(any(String.class))).thenReturn(patientId);

        when(adapterProxyFactory.create()).thenReturn(adapterProxy);
        when(adapterProxy.processPatientDiscoveryAsyncResp(request, assertion)).thenReturn(expectedResponse);

        // Actual invocation
        StandardInboundPatientDiscoveryDeferredResponse standardPatientDiscovery
            = new StandardInboundPatientDiscoveryDeferredResponse(
                policyChecker, responseFactory, msgProcessor, adapterProxyFactory, pdCorrelationDao, auditLogger);

        MCCIIN000002UV01 actualResponse = standardPatientDiscovery.respondingGatewayDeferredPRPAIN201306UV02(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);

        // Verify policy check is with the correct request
        ArgumentCaptor<RespondingGatewayPRPAIN201306UV02RequestType> policyReqArgument = ArgumentCaptor
            .forClass(RespondingGatewayPRPAIN201306UV02RequestType.class);

        verify(policyChecker).checkOutgoingPolicy(policyReqArgument.capture());
        assertEquals(request, policyReqArgument.getValue().getPRPAIN201306UV02());

        // Verify response mode is called
        verify(responseMode).processResponse(request, assertion, patientId);

        // Verify audits
        verify(mockEJBLogger, never()).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion),
            isNull(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties),
            eq(NhincConstants.PATIENT_DISCOVERY_DEFERRED_RESP_SERVICE_NAME),
            any(PatientDiscoveryDeferredResponseAuditTransforms.class));
    }

    private PatientDiscoveryDeferredResponseAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new PatientDiscoveryDeferredResponseAuditLogger() {
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
