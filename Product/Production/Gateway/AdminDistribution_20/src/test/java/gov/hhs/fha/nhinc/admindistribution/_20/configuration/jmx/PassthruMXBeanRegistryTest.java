/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.admindistribution._20.configuration.jmx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import gov.hhs.fha.nhinc.admindistribution.configuration.jmx.AdminDistribution20WebServices;
import gov.hhs.fha.nhinc.configuration.IConfiguration.directionEnum;
import gov.hhs.fha.nhinc.configuration.IConfiguration.serviceEnum;
import gov.hhs.fha.nhinc.configuration.jmx.PassthruMXBeanRegistry;
import org.junit.Test;

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
        serviceEnum serviceName = serviceEnum.AdminDistribution;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        AdminDistribution20WebServices adminDist20 = mock(AdminDistribution20WebServices.class);
        when(adminDist20.getServiceName()).thenReturn(serviceEnum.AdminDistribution);
        when(adminDist20.isOutboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(adminDist20);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testSetInboundPassthruMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.AdminDistribution;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        AdminDistribution20WebServices adminDist20 = mock(AdminDistribution20WebServices.class);
        when(adminDist20.getServiceName()).thenReturn(serviceEnum.AdminDistribution);
        when(adminDist20.isInboundPassthru()).thenReturn(status);
        registry.registerWebServiceMXBean(adminDist20);
        boolean passthru = registry.isPassthru(serviceName, direction);
        assertEquals(true, passthru);
    }

    @Test
    public void testSetOutboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.AdminDistribution;
        directionEnum direction = directionEnum.Outbound;
        boolean status = true;
        AdminDistribution20WebServices adminDist20 = mock(AdminDistribution20WebServices.class);
        when(adminDist20.getServiceName()).thenReturn(serviceEnum.AdminDistribution);
        when(adminDist20.isOutboundStandard()).thenReturn(status);
        registry.registerWebServiceMXBean(adminDist20);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

    @Test
    public void testSetInboundStandardMode() {
        PassthruMXBeanRegistry registry = PassthruMXBeanRegistry.getInstance();
        serviceEnum serviceName = serviceEnum.AdminDistribution;
        directionEnum direction = directionEnum.Inbound;
        boolean status = true;
        AdminDistribution20WebServices adminDist20 = mock(AdminDistribution20WebServices.class);
        when(adminDist20.getServiceName()).thenReturn(serviceEnum.AdminDistribution);
        when(adminDist20.isInboundStandard()).thenReturn(status);
        registry.registerWebServiceMXBean(adminDist20);
        boolean standard = registry.isStandard(serviceName, direction);
        assertEquals(true, standard);
    }

}
