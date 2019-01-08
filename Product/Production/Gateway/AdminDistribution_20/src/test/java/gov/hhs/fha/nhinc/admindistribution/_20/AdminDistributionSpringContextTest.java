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
package gov.hhs.fha.nhinc.admindistribution._20;

import static org.junit.Assert.assertNotNull;

import gov.hhs.fha.nhinc.admindistribution._20.entity.EntityAdministrativeDistributionSecured_g1;
import gov.hhs.fha.nhinc.admindistribution._20.entity.EntityAdministrativeDistribution_g1;
import gov.hhs.fha.nhinc.admindistribution._20.nhin.NhinAdministrativeDistribution_g1;
import gov.hhs.fha.nhinc.admindistribution.inbound.PassthroughInboundAdminDistribution;
import gov.hhs.fha.nhinc.admindistribution.inbound.StandardInboundAdminDistribution;
import gov.hhs.fha.nhinc.admindistribution.outbound.PassthroughOutboundAdminDistribution;
import gov.hhs.fha.nhinc.admindistribution.outbound.StandardOutboundAdminDistribution;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageSecuredType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewaySendAlertMessageType;
import oasis.names.tc.emergency.edxl.de._1.EDXLDistribution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author akong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/admindistribution/_20/applicationContext.xml" })
public class AdminDistributionSpringContextTest {

    @Autowired
    StandardOutboundAdminDistribution standardOutboundOrchImpl;

    @Autowired
    StandardInboundAdminDistribution standardInboundOrchImpl;

    @Autowired
    PassthroughInboundAdminDistribution passthroughInboundOrchImpl;

    @Autowired
    PassthroughOutboundAdminDistribution passthroughOutboundOrchImpl;

    @Autowired
    NhinAdministrativeDistribution_g1 inboundAdminDist;

    @Autowired
    EntityAdministrativeDistribution_g1 outboundAdminDistUnsecuredEndpoint;

    @Autowired
    EntityAdministrativeDistributionSecured_g1 outboundAdminDistSecuredEndpoint;

    @Test
    public void inbound() {
        assertNotNull(inboundAdminDist);

        EDXLDistribution request = new EDXLDistribution();
        inboundAdminDist.sendAlertMessage(request);
    }

    @Test
    public void outboundUnsecured() {
        assertNotNull(outboundAdminDistUnsecuredEndpoint);

        RespondingGatewaySendAlertMessageType request = new RespondingGatewaySendAlertMessageType();
        outboundAdminDistUnsecuredEndpoint.sendAlertMessage(request);
    }

    @Test
    public void outboundSecured() {
        assertNotNull(outboundAdminDistSecuredEndpoint);

        RespondingGatewaySendAlertMessageSecuredType request = new RespondingGatewaySendAlertMessageSecuredType();
        outboundAdminDistSecuredEndpoint.sendAlertMessage(request);
    }

}
