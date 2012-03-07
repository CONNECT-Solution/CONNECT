/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services. 
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
package gov.hhs.fha.nhinc.docretrieve.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.orchestration.CONNECTOutboundOrchestrator;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.Orchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveOrchestratorImpl extends CONNECTOutboundOrchestrator {

    private static final Log logger = LogFactory.getLog(OutboundDocRetrieveOrchestratorImpl.class);

    @Override
    public Orchestratable processEnabledMessage(Orchestratable message) {
        OutboundDocRetrieveOrchestratable EntityDROrchMessage = (OutboundDocRetrieveOrchestratable) message;
        for (DocumentRequest docRequest : EntityDROrchMessage.getRequest().getDocumentRequest()) {
            OutboundOrchestratable impl = new OutboundDocRetrieveOrchestratable(message.getPolicyTransformer(),
                    message.getAuditTransformer(), EntityDROrchMessage.getNhinDelegate(),
                    EntityDROrchMessage.getAggregator());
            RetrieveDocumentSetRequestType rdRequest = new RetrieveDocumentSetRequestType();
            rdRequest.getDocumentRequest().add(docRequest);
            ((OutboundDocRetrieveOrchestratable) impl).setRequest(rdRequest);
            ((OutboundDocRetrieveOrchestratable) impl).setAssertion(message.getAssertion());
            ((OutboundDocRetrieveOrchestratable) impl).setTarget(buildHomeCommunity(docRequest.getHomeCommunityId()));

            // Process and aggregate
            NhinAggregator agg = EntityDROrchMessage.getAggregator();
            agg.aggregate((OutboundOrchestratable) message, (OutboundOrchestratable) super.processEnabledMessage(impl));
        }
        return message;
    }

    private NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }

    @Override
    public Log getLogger() {
        return logger;
    }
}
