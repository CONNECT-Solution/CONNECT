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
package gov.hhs.fha.nhinc.admindistribution.inbound;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionAuditLogger;
import gov.hhs.fha.nhinc.admindistribution.AdminDistributionUtils;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxy;
import gov.hhs.fha.nhinc.admindistribution.adapter.proxy.AdapterAdminDistributionProxyObjectFactory;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.largefile.LargePayloadException;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;

import org.junit.Test;

/**
 * @author akong
 * 
 */
public class PassthroughInboundAdminDistributionTest {

    @Test
    public void passthroughAdminDistribution() {
        EDXLDistribution request = new EDXLDistribution();
        AssertionType assertion = new AssertionType();
        
        AdminDistributionUtils adminUtils = mock(AdminDistributionUtils.class);
        AdapterAdminDistributionProxyObjectFactory adapterFactory = mock(AdapterAdminDistributionProxyObjectFactory.class);
        AdapterAdminDistributionProxy adapterProxy = mock(AdapterAdminDistributionProxy.class);
        AdminDistributionAuditLogger auditLogger = mock(AdminDistributionAuditLogger.class);
        
        when(adapterFactory.getAdapterAdminDistProxy()).thenReturn(adapterProxy);

        PassthroughInboundAdminDistribution passthroughAdminDist = new PassthroughInboundAdminDistribution(auditLogger, 
        		adminUtils, adapterFactory);

        passthroughAdminDist.sendAlertMessage(request, assertion);

        verify(adapterProxy).sendAlertMessage(eq(request), eq(assertion));
        
        verify(auditLogger, times(1)).auditNhinAdminDist(any(EDXLDistribution.class),
        		any(AssertionType.class), any(String.class), any(NhinTargetSystemType.class), 
        		any(String.class));
        
    }
    
    @Test
    public void convertDataToFileError() throws LargePayloadException {
        EDXLDistribution request = new EDXLDistribution();
        AssertionType assertion = new AssertionType();
        LargePayloadException exception = new LargePayloadException();

        AdminDistributionUtils adminUtils = mock(AdminDistributionUtils.class);
        AdapterAdminDistributionProxyObjectFactory adapterFactory = mock(AdapterAdminDistributionProxyObjectFactory.class);
        AdminDistributionAuditLogger auditLogger = new AdminDistributionAuditLogger();
        
        doThrow(exception).when(adminUtils).convertDataToFileLocationIfEnabled(request);
        
        PassthroughInboundAdminDistribution passthroughAdminDist = new PassthroughInboundAdminDistribution(auditLogger,
        		adminUtils, adapterFactory);

        passthroughAdminDist.sendAlertMessage(request, assertion);
        
    }
}
