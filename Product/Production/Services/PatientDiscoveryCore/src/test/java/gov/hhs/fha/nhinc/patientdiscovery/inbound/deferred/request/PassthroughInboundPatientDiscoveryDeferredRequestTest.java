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
package gov.hhs.fha.nhinc.patientdiscovery.inbound.deferred.request;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.request.proxy.AdapterPatientDiscoveryDeferredReqProxyObjectFactory;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryDeferredRequestAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryDeferredRequestAuditTransforms;
import java.util.Properties;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class PassthroughInboundPatientDiscoveryDeferredRequestTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);

    @Test
    public void passthroughInboundPatientDiscoveryRequest() {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        AdapterPatientDiscoveryDeferredReqProxyObjectFactory adapterFactory
            = mock(AdapterPatientDiscoveryDeferredReqProxyObjectFactory.class);
        AdapterPatientDiscoveryDeferredReqProxy adapterProxy = mock(AdapterPatientDiscoveryDeferredReqProxy.class);
        PatientDiscoveryDeferredRequestAuditLogger auditLogger = getAuditLogger(true);
        Properties webContextProperties = new Properties();
        when(adapterFactory.getAdapterPatientDiscoveryDeferredReqProxy()).thenReturn(adapterProxy);

        when(adapterProxy.processPatientDiscoveryAsyncReq(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundPatientDiscoveryDeferredRequest passthroughPatientDiscovery
            = new PassthroughInboundPatientDiscoveryDeferredRequest(adapterFactory, auditLogger);

        MCCIIN000002UV01 actualResponse = passthroughPatientDiscovery.respondingGatewayPRPAIN201305UV02(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);
        verify(mockEJBLogger).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion), isNull(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties), eq(
            NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME), any(
                PatientDiscoveryDeferredRequestAuditTransforms.class));
    }

    @Test
    public void auditOffForInboundPDDeferredReq() {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        AssertionType assertion = new AssertionType();
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        AdapterPatientDiscoveryDeferredReqProxyObjectFactory adapterFactory
            = mock(AdapterPatientDiscoveryDeferredReqProxyObjectFactory.class);
        AdapterPatientDiscoveryDeferredReqProxy adapterProxy = mock(AdapterPatientDiscoveryDeferredReqProxy.class);
        PatientDiscoveryDeferredRequestAuditLogger auditLogger = getAuditLogger(false);
        Properties webContextProperties = new Properties();
        when(adapterFactory.getAdapterPatientDiscoveryDeferredReqProxy()).thenReturn(adapterProxy);

        when(adapterProxy.processPatientDiscoveryAsyncReq(request, assertion)).thenReturn(expectedResponse);

        PassthroughInboundPatientDiscoveryDeferredRequest passthroughPatientDiscovery
            = new PassthroughInboundPatientDiscoveryDeferredRequest(adapterFactory, auditLogger);

        MCCIIN000002UV01 actualResponse = passthroughPatientDiscovery.respondingGatewayPRPAIN201305UV02(request,
            assertion, webContextProperties);

        assertSame(expectedResponse, actualResponse);
        verify(mockEJBLogger, never()).auditResponseMessage(eq(request), eq(actualResponse), eq(assertion), isNull(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_INBOUND_DIRECTION), eq(
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.FALSE), eq(webContextProperties), eq(
            NhincConstants.PATIENT_DISCOVERY_DEFERRED_REQ_SERVICE_NAME), any(
                PatientDiscoveryDeferredRequestAuditTransforms.class));
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
