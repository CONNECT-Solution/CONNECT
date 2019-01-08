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
package gov.hhs.fha.nhinc.docsubmission._11.configuration.jmx;

import gov.hhs.fha.nhinc.configuration.IConfiguration.directionEnum;
import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.configuration.jmx.PassthruMXBeanRegistry;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmission11WebServices;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmissionDefRequest11WebServices;
import gov.hhs.fha.nhinc.docsubmission.configuration.jmx.DocumentSubmissionDefResponse11WebServices;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author achidambaram
 *
 */
public class PassthruMXBeanRegistryTest {
    @Test
    public void testGetInstance() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        assertNotNull(registry);
    }

    @Test
    public void testSetOutboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmission;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        DocumentSubmission11WebServices docSubmission11 = mock(DocumentSubmission11WebServices.class);
        when(docSubmission11.getServiceName()).thenReturn(serviceEnum.DocumentSubmission);
        when(docSubmission11.isOutboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmission11);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testSetInboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmission;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        DocumentSubmission11WebServices docSubmission11 = mock(DocumentSubmission11WebServices.class);
        when(docSubmission11.getServiceName()).thenReturn(serviceEnum.DocumentSubmission);
        when(docSubmission11.isInboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmission11);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testSetOutboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmission;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        DocumentSubmission11WebServices docSubmission11 = mock(DocumentSubmission11WebServices.class);
        when(docSubmission11.getServiceName()).thenReturn(serviceEnum.DocumentSubmission);
        when(docSubmission11.isOutboundStandard()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmission11);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

    @Test
    public void testSetInboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmission;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        DocumentSubmission11WebServices docSubmission11 = mock(DocumentSubmission11WebServices.class);
        when(docSubmission11.getServiceName()).thenReturn(serviceEnum.DocumentSubmission);
        when(docSubmission11.isInboundStandard()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmission11);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

    @Test
    public void testPDDeferredReqSetOutboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredRequest;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        DocumentSubmissionDefRequest11WebServices docSubmissionDeferredReq = mock(DocumentSubmissionDefRequest11WebServices.class);
        when(docSubmissionDeferredReq.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredRequest);
        when(docSubmissionDeferredReq.isOutboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmissionDeferredReq);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testPDDeferredReqSetInboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredRequest;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        DocumentSubmissionDefRequest11WebServices docSubmissionDeferredReq = mock(DocumentSubmissionDefRequest11WebServices.class);
        when(docSubmissionDeferredReq.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredRequest);
        when(docSubmissionDeferredReq.isInboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmissionDeferredReq);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testPDDeferredReqSetOutboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredRequest;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        DocumentSubmissionDefRequest11WebServices docSubmissionDeferredReq = mock(DocumentSubmissionDefRequest11WebServices.class);
        when(docSubmissionDeferredReq.isOutboundStandard()).thenReturn(status);
        when(docSubmissionDeferredReq.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredRequest);
        registry.registerWebServiceMXBean(docSubmissionDeferredReq);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

    @Test
    public void testPDDeferredReqSetInboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredRequest;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        DocumentSubmissionDefRequest11WebServices docSubmissionDeferredReq = mock(DocumentSubmissionDefRequest11WebServices.class);
        when(docSubmissionDeferredReq.isInboundStandard()).thenReturn(status);
        when(docSubmissionDeferredReq.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredRequest);
        registry.registerWebServiceMXBean(docSubmissionDeferredReq);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

    @Test
    public void testPDDeferredRespSetOutboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredResponse;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        DocumentSubmissionDefResponse11WebServices docSubmisisonDeferredResp = mock(DocumentSubmissionDefResponse11WebServices.class);
        when(docSubmisisonDeferredResp.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredResponse);
        when(docSubmisisonDeferredResp.isOutboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmisisonDeferredResp);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testPDDeferredRespSetInboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredResponse;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        DocumentSubmissionDefResponse11WebServices docSubmisisonDeferredResp = mock(DocumentSubmissionDefResponse11WebServices.class);
        when(docSubmisisonDeferredResp.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredResponse);
        when(docSubmisisonDeferredResp.isInboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmisisonDeferredResp);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testPDDeferredRespSetOutboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredResponse;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        DocumentSubmissionDefResponse11WebServices docSubmisisonDeferredResp = mock(DocumentSubmissionDefResponse11WebServices.class);
        when(docSubmisisonDeferredResp.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredResponse);
        when(docSubmisisonDeferredResp.isOutboundStandard()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmisisonDeferredResp);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

    @Test
    public void testPDDeferredRespSetInboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.DocumentSubmissionDeferredResponse;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        DocumentSubmissionDefResponse11WebServices docSubmisisonDeferredResp = mock(DocumentSubmissionDefResponse11WebServices.class);
        when(docSubmisisonDeferredResp.getServiceName()).thenReturn(serviceEnum.DocumentSubmissionDeferredResponse);
        when(docSubmisisonDeferredResp.isInboundStandard()).thenReturn(status);
        registry.registerWebServiceMXBean(docSubmisisonDeferredResp);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }


}
