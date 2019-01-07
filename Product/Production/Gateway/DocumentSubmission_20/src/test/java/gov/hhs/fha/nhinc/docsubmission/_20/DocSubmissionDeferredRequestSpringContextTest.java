/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
 *  All rights reserved.
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
package gov.hhs.fha.nhinc.docsubmission._20;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission._20.entity.deferred.request.EntityDocSubmissionDeferredRequestSecured_g1;
import gov.hhs.fha.nhinc.docsubmission._20.entity.deferred.request.EntityDocSubmissionDeferredRequestUnsecured_g1;
import gov.hhs.fha.nhinc.docsubmission._20.nhin.deferred.request.NhinXDRRequest20;
import gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request.PassthroughInboundDocSubmissionDeferredRequest;
import gov.hhs.fha.nhinc.docsubmission.inbound.deferred.request.StandardInboundDocSubmissionDeferredRequest;
import gov.hhs.fha.nhinc.docsubmission.outbound.deferred.request.PassthroughOutboundDocSubmissionDeferredRequest;
import gov.hhs.fha.nhinc.docsubmission.outbound.deferred.request.StandardOutboundDocSubmissionDeferredRequest;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import static org.junit.Assert.assertNotNull;
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
@ContextConfiguration(locations = { "/docsubmission/_20/applicationContext.xml" })
public class DocSubmissionDeferredRequestSpringContextTest {

    @Autowired
    NhinXDRRequest20 inboundDocSubmissionRequestEndpoint;

    @Autowired
    EntityDocSubmissionDeferredRequestUnsecured_g1 outboundDocSubmissionRequestUnsecuredEndpoint;

    @Autowired
    EntityDocSubmissionDeferredRequestSecured_g1 outboundDocSubmissionRequestSecuredEndpoint;

    @Autowired
    StandardOutboundDocSubmissionDeferredRequest stdOutboundDocSubmissionDeferredRequest;

    @Autowired
    PassthroughOutboundDocSubmissionDeferredRequest ptOutboundDocSubmissionDeferredRequest;

    @Autowired
    StandardInboundDocSubmissionDeferredRequest stdInboundDocSubmissionDeferredRequest;

    @Autowired
    PassthroughInboundDocSubmissionDeferredRequest ptInbounDocSubmissionDeferredRequest;


    @Test
    public void inbound() {
        assertNotNull(inboundDocSubmissionRequestEndpoint);

        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();
        RegistryResponseType response = inboundDocSubmissionRequestEndpoint.provideAndRegisterDocumentSetBDeferredRequest(request);

        assertNotNull(response);
    }

    @Test
    public void outboundUnsecured() {
        assertNotNull(outboundDocSubmissionRequestUnsecuredEndpoint);

        RespondingGatewayProvideAndRegisterDocumentSetRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetRequestType();
        XDRAcknowledgementType response = outboundDocSubmissionRequestUnsecuredEndpoint
                .provideAndRegisterDocumentSetBAsyncRequest(request);

        assertNotNull(response);
    }

    @Test
    public void outboundSecured() {
        assertNotNull(outboundDocSubmissionRequestSecuredEndpoint);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType();
        XDRAcknowledgementType response = outboundDocSubmissionRequestSecuredEndpoint
                .provideAndRegisterDocumentSetBAsyncRequest(request);

        assertNotNull(response);
    }

}
