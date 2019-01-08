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
package gov.hhs.fha.nhinc.docretrieve._30;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.docretrieve._30.entity.EntityDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve._30.entity.EntityDocRetrieveSecured;
import gov.hhs.fha.nhinc.docretrieve.inbound.DocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.inbound.PassthroughInboundDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.inbound.StandardInboundDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.outbound.PassthroughOutboundDocRetrieve;
import gov.hhs.fha.nhinc.docretrieve.outbound.StandardOutboundDocRetrieve;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
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
@ContextConfiguration(locations = { "/docretrieve/_30/applicationContext.xml" })
public class DocRetrieveSpringContextTest {

	@Autowired
    StandardOutboundDocRetrieve standardOutboundOrchImpl;

    @Autowired
    StandardInboundDocRetrieve standardInboundOrchImpl;

    @Autowired
    PassthroughInboundDocRetrieve passthroughInboundOrchImpl;

    @Autowired
    PassthroughOutboundDocRetrieve passthroughOutboundOrchImpl;

    @Autowired
    DocRetrieve inboundDocRetrieve;

    @Autowired
    EntityDocRetrieve outboundDocRetrieveUnsecured;

    @Autowired
    EntityDocRetrieveSecured outboundDocRetrieveSecured;

    @Test
    public void inbound() {
        assertNotNull(inboundDocRetrieve);

        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        RetrieveDocumentSetResponseType response = inboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(request);

        assertNotNull(response);
    }

    @Test
    public void outboundUnsecured() {
        assertNotNull(outboundDocRetrieveUnsecured);

        RespondingGatewayCrossGatewayRetrieveRequestType request = new RespondingGatewayCrossGatewayRetrieveRequestType();
        RetrieveDocumentSetResponseType response = outboundDocRetrieveUnsecured.respondingGatewayCrossGatewayRetrieve(request);

        assertNotNull(response);
    }

    @Test
    public void outboundSecured() {
        assertNotNull(outboundDocRetrieveSecured);

        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();
        DocumentRequest document = new DocumentRequest();
        document.setHomeCommunityId("2.2");
        document.setRepositoryUniqueId("1");
        request.getDocumentRequest().add(document);
        RetrieveDocumentSetResponseType response = outboundDocRetrieveSecured.respondingGatewayCrossGatewayRetrieve(request);

        assertNotNull(response);
    }

}
