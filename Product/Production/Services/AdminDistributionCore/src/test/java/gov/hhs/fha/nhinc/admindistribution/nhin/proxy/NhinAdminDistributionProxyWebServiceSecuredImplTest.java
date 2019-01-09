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
package gov.hhs.fha.nhinc.admindistribution.nhin.proxy;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import gov.hhs.fha.nhinc.admindistribution.AdminDistributionHelper;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.messaging.client.CONNECTClient;
import gov.hhs.fha.nhinc.messaging.service.port.ServicePortDescriptor;
import gov.hhs.fha.nhinc.nhinadmindistribution.RespondingGatewayAdministrativeDistributionPortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author msw
 *
 */
public class NhinAdminDistributionProxyWebServiceSecuredImplTest {

    @SuppressWarnings("unchecked")
    private final CONNECTClient<RespondingGatewayAdministrativeDistributionPortType> client = mock(CONNECTClient.class);
    private final EDXLDistribution body = mock(EDXLDistribution.class);
    private final AssertionType assertion = mock(AssertionType.class);
    private final NhinTargetSystemType target = mock(NhinTargetSystemType.class);

    @Before
    public void before() {
        Mockito.when(target.getHomeCommunity()).thenReturn(new HomeCommunityType());
    }

    @Test
    public void testNoMtom() {
        NhinAdminDistributionProxyWebServiceSecuredImpl impl = getImpl();

        GATEWAY_API_LEVEL apiLevel = GATEWAY_API_LEVEL.LEVEL_g0;
        impl.sendAlertMessage(body, assertion, target, apiLevel);
        verify(client, never()).enableMtom();
    }

    @Test
    public void testMtom() {
        NhinAdminDistributionProxyWebServiceSecuredImpl impl = getImpl();

        GATEWAY_API_LEVEL apiLevel = GATEWAY_API_LEVEL.LEVEL_g1;
        impl.sendAlertMessage(body, assertion, target, apiLevel);
        verify(client).enableMtom();
    }

    private NhinAdminDistributionProxyWebServiceSecuredImpl getImpl() {
        return new NhinAdminDistributionProxyWebServiceSecuredImpl() {

            @Override
            protected AdminDistributionHelper getHelper() {
               AdminDistributionHelper helperMock = Mockito.mock(AdminDistributionHelper.class);
               Mockito.when(helperMock.getUrl(Mockito.any(NhinTargetSystemType.class), Mockito.anyString(), Mockito.any(GATEWAY_API_LEVEL.class))).thenReturn("URL");
               return helperMock;
            }

            @Override
            protected CONNECTClient<RespondingGatewayAdministrativeDistributionPortType> getCONNECTClientSecured(
                    ServicePortDescriptor<RespondingGatewayAdministrativeDistributionPortType> portDescriptor,
                    String url, AssertionType assertion, NhinTargetSystemType target, String serviceName) {
                return client;
            }
        };
    }
}
