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
package gov.hhs.fha.nhinc.docretrieve._30.entity;

import gov.hhs.fha.nhinc.aspect.OutboundMessageEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetRequestTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.aspect.RetrieveDocumentSetResponseTypeDescriptionBuilder;
import gov.hhs.fha.nhinc.docretrieve.outbound.OutboundDocRetrieve;
import gov.hhs.fha.nhinc.entitydocretrievesecured.EntityDocRetrieveSecuredPortType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import javax.annotation.Resource;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.SOAPBinding;

@BindingType(value = SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieveSecured extends BaseService implements EntityDocRetrieveSecuredPortType {

    private OutboundDocRetrieve outboundDocRetrieve;

    private WebServiceContext context;

    @OutboundMessageEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class, afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, serviceType = "Retrieve Document", version = "3.0")
    @Override
    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(RetrieveDocumentSetRequestType body) {
        AssertionType assertion = getAssertion(context, null);

        if (assertion != null) {
            assertion.setImplementsSpecVersion(NhincConstants.UDDI_SPEC_VERSION.SPEC_3_0.toString());
        }

        return outboundDocRetrieve.respondingGatewayCrossGatewayRetrieve(body, assertion, createNhinTargetCommunities(body),
            ADAPTER_API_LEVEL.LEVEL_a1);
    }

    private NhinTargetCommunitiesType createNhinTargetCommunities(RetrieveDocumentSetRequestType body) {
        NhinTargetCommunitiesType targets = new NhinTargetCommunitiesType();
        NhinTargetCommunityType target = new NhinTargetCommunityType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(body.getDocumentRequest().get(0).getHomeCommunityId());
        target.setHomeCommunity(homeCommunity);
        targets.getNhinTargetCommunity().add(target);
        targets.setUseSpecVersion("3.0");
        return targets;
    }

    @Resource
    public void setContext(WebServiceContext context) {
        this.context = context;
    }

    public void setOutboundDocRetrieve(OutboundDocRetrieve outboundDocRetrieve) {
        this.outboundDocRetrieve = outboundDocRetrieve;
    }
}
