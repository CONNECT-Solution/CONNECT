/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 * 
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

@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityDocRetrieveSecured extends BaseService implements EntityDocRetrieveSecuredPortType {

    private OutboundDocRetrieve outboundDocRetrieve;

    private WebServiceContext context;

    @OutboundMessageEvent(beforeBuilder = RetrieveDocumentSetRequestTypeDescriptionBuilder.class, afterReturningBuilder = RetrieveDocumentSetResponseTypeDescriptionBuilder.class, serviceType = "Retrieve Document", version = "3.0")
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
