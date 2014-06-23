/*
 * Copyright (c) 2009-2014, United States Government, as represented by the Secretary of Health and Human Services.
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

import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditor;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxy;
import gov.hhs.fha.nhinc.patientdiscovery.adapter.deferred.response.proxy.AdapterPatientDiscoveryDeferredRespProxyObjectFactory;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201306UV02;
import org.junit.Test;

/**
 * @author akong
 * 
 */
public class PassthroughInboundPatientDiscoveryDeferredResponseTest {

    @Test
    public void invoke() {
        PRPAIN201306UV02 request = new PRPAIN201306UV02();
        AssertionType assertion = new AssertionType();
        MCCIIN000002UV01 expectedResponse = new MCCIIN000002UV01();

        // Mocks
        AdapterPatientDiscoveryDeferredRespProxyObjectFactory adapterFactory = mock(AdapterPatientDiscoveryDeferredRespProxyObjectFactory.class);
        AdapterPatientDiscoveryDeferredRespProxy adapterProxy = mock(AdapterPatientDiscoveryDeferredRespProxy.class);
        PatientDiscoveryAuditor auditLogger = mock(PatientDiscoveryAuditor.class);

        // Stubbing the methods
        when(adapterFactory.create()).thenReturn(adapterProxy);

        when(adapterProxy.processPatientDiscoveryAsyncResp(request, assertion)).thenReturn(expectedResponse);

        // Actual invocation
        PassthroughInboundPatientDiscoveryDeferredResponse passthroughPatientDiscovery = new PassthroughInboundPatientDiscoveryDeferredResponse(
                adapterFactory, auditLogger);

        MCCIIN000002UV01 actualResponse = passthroughPatientDiscovery.respondingGatewayDeferredPRPAIN201306UV02(
                request, assertion);

        // Verify
        assertSame(expectedResponse, actualResponse);

        verify(auditLogger, never()).auditAdapterDeferred201306(any(PRPAIN201306UV02.class), any(AssertionType.class),
                any(String.class));

        verify(auditLogger, never()).auditAck(any(MCCIIN000002UV01.class), any(AssertionType.class), any(String.class),
                eq(NhincConstants.AUDIT_LOG_ADAPTER_INTERFACE));

        verify(auditLogger).auditNhinDeferred201306(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);

        verify(auditLogger).auditAck(actualResponse, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
                NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

    }

}
