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
package gov.hhs.fha.nhinc.docsubmission._11;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType;
import gov.hhs.fha.nhinc.docsubmission._11.entity.deferred.response.EntityDocSubmissionDeferredResponseSecured;
import gov.hhs.fha.nhinc.docsubmission._11.entity.deferred.response.EntityDocSubmissionDeferredResponseUnsecured;
import gov.hhs.fha.nhinc.docsubmission._11.nhin.deferred.response.NhinXDRResponse;
import gov.hhs.fha.nhinc.docsubmission.inbound.deferred.response.PassthroughInboundDocSubmissionDeferredResponse;
import gov.hhs.fha.nhinc.docsubmission.inbound.deferred.response.StandardInboundDocSubmissionDeferredResponse;
import gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response.PassthroughOutboundDocSubmissionDeferredResponse;
import gov.hhs.fha.nhinc.docsubmission.outbound.deferred.response.StandardOutboundDocSubmissionDeferredResponse;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
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
@ContextConfiguration(locations = { "/docsubmission/_11/applicationContext.xml" })
public class DocSubmissionDeferredResponseSpringContextTest {

    @Autowired
    NhinXDRResponse inboundDocSubmissionResponseEndpoint;

    @Autowired
    EntityDocSubmissionDeferredResponseUnsecured outboundDocSubmissionResponseUnsecuredEndpoint;

    @Autowired
    EntityDocSubmissionDeferredResponseSecured outboundDocSubmissionResponseSecuredEndpoint;

    @Autowired
    StandardOutboundDocSubmissionDeferredResponse stdOutboundDocSubmissionDeferredResponse;

    @Autowired
    PassthroughOutboundDocSubmissionDeferredResponse ptOutboundDocSubmissionDeferredResponse;

    @Autowired
    StandardInboundDocSubmissionDeferredResponse stdInboundDocSubmissionDeferredResponse;

    @Autowired
    PassthroughInboundDocSubmissionDeferredResponse ptInbounDocSubmissionDeferredResponse;

    @Test
    public void inbound() {
        assertNotNull(inboundDocSubmissionResponseEndpoint);

        RegistryResponseType request = new RegistryResponseType();
        XDRAcknowledgementType response = inboundDocSubmissionResponseEndpoint
                .provideAndRegisterDocumentSetBDeferredResponse(request);

        assertNotNull(response);
    }

    @Test
    public void outboundUnsecured() {
        assertNotNull(outboundDocSubmissionResponseUnsecuredEndpoint);

        RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType();
        XDRAcknowledgementType response = outboundDocSubmissionResponseUnsecuredEndpoint
                .provideAndRegisterDocumentSetBAsyncResponse(request);

        assertNotNull(response);
    }

    @Test
    public void outboundSecured() {
        assertNotNull(outboundDocSubmissionResponseSecuredEndpoint);

        RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType request = new RespondingGatewayProvideAndRegisterDocumentSetSecuredResponseRequestType();
        XDRAcknowledgementType response = outboundDocSubmissionResponseSecuredEndpoint
                .provideAndRegisterDocumentSetBAsyncResponse(request);

        assertNotNull(response);
    }

}
