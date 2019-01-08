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
package gov.hhs.fha.nhinc.patientdiscovery._10.gateway.ws;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryException;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.InboundPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.PassthroughInboundPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.inbound.StandardInboundPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.PassthroughOutboundPatientDiscovery;
import gov.hhs.fha.nhinc.patientdiscovery.outbound.StandardOutboundPatientDiscovery;
import ihe.iti.xcpd._2009.PRPAIN201305UV02Fault;
import java.util.Properties;
import javax.xml.ws.WebServiceContext;
import org.apache.cxf.jaxws.context.WebServiceContextImpl;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author akong
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/patientdiscovery/_10/applicationContext.xml"})
public class PatientDiscoverySpringContextTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    NhinPatientDiscovery inboundPatientDiscoveryEndpoint;

    @Autowired
    EntityPatientDiscoveryUnsecured outboundPatientDiscoveryUnsecuredEndpoint;

    @Autowired
    EntityPatientDiscoverySecured outboundPatientDiscoverySecuredEndpoint;

    @Autowired
    StandardOutboundPatientDiscovery standardOutboundOrchImpl;

    @Autowired
    StandardInboundPatientDiscovery standardInboundOrchImpl;

    @Autowired
    PassthroughInboundPatientDiscovery passthroughInboundOrchImpl;

    @Autowired
    PassthroughOutboundPatientDiscovery passthroughOutboundOrchImpl;

    @Test
    public void inbound() throws PRPAIN201305UV02Fault {
        assertNotNull(inboundPatientDiscoveryEndpoint);
        PRPAIN201305UV02 request = new PRPAIN201305UV02();
        WebServiceContext context = new WebServiceContextImpl();
        inboundPatientDiscoveryEndpoint.setContext(context);
        PRPAIN201306UV02 response = inboundPatientDiscoveryEndpoint.respondingGatewayPRPAIN201305UV02(request);
        assertNotNull(response);
    }

    @Ignore
    @Test
    public void inboundFault() throws PatientDiscoveryException {
        PRPAIN201305UV02 request = new PRPAIN201305UV02();

        NhinPatientDiscovery inboundPDEndpoint = new NhinPatientDiscovery();

        InboundPatientDiscovery inboundPatientDiscovery = mock(InboundPatientDiscovery.class);

        when(inboundPatientDiscovery.respondingGatewayPRPAIN201305UV02(eq(request), any(AssertionType.class),
            any(Properties.class)))
            .thenThrow(new PatientDiscoveryException(""));

        inboundPDEndpoint.setInboundPatientDiscovery(inboundPatientDiscovery);

        boolean faultThrown = false;
        try {
            inboundPDEndpoint.respondingGatewayPRPAIN201305UV02(request);
        } catch (PRPAIN201305UV02Fault fault) {
            faultThrown = true;
            assertEquals("920", fault.getFaultInfo().getErrorCode());
        }

        assertTrue(faultThrown);
    }

    @Test
    public void outboundUnsecured() {
        assertNotNull(outboundPatientDiscoveryUnsecuredEndpoint);

        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        RespondingGatewayPRPAIN201306UV02ResponseType response = outboundPatientDiscoveryUnsecuredEndpoint
            .respondingGatewayPRPAIN201305UV02(request);

        assertNotNull(response);
    }

    @Test
    public void outboundSecured() {
        assertNotNull(outboundPatientDiscoverySecuredEndpoint);

        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        RespondingGatewayPRPAIN201306UV02ResponseType response = outboundPatientDiscoverySecuredEndpoint
            .respondingGatewayPRPAIN201305UV02(request);

        assertNotNull(response);
    }
}
