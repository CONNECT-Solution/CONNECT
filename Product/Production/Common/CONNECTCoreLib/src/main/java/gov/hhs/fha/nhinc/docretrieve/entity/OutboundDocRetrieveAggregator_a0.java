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

import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.orchestration.NhinAggregator;
import gov.hhs.fha.nhinc.orchestration.OutboundOrchestratable;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author mweaver
 */
public class OutboundDocRetrieveAggregator_a0 implements NhinAggregator {

    private static final Log logger = LogFactory.getLog(OutboundDocRetrieveAggregator_a0.class);

    private Log getLogger() {
        return logger;
    }

    public void aggregate(OutboundOrchestratable to, OutboundOrchestratable from) {
        if (to instanceof OutboundDocRetrieveOrchestratable) {
            if (from instanceof OutboundDocRetrieveOrchestratable) {
                OutboundDocRetrieveOrchestratable to_a0 = (OutboundDocRetrieveOrchestratable) to;
                OutboundDocRetrieveOrchestratable from_a0 = (OutboundDocRetrieveOrchestratable) from;
                if (to_a0.getResponse() == null) {
                    to_a0.setResponse(new RetrieveDocumentSetResponseType());
                }

                if (to_a0.getResponse().getRegistryResponse() == null) {
                    RegistryResponseType rrt = new RegistryResponseType();
                    to_a0.getResponse().setRegistryResponse(rrt);
                }
                
                if (from_a0.getResponse() == null
                        || from_a0.getResponse().getRegistryResponse() == null
                        || NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE.equalsIgnoreCase(from_a0.getResponse()
                                .getRegistryResponse().getStatus())) {
                    to_a0.getResponse().getRegistryResponse()
                            .setStatus(NhincConstants.NHINC_ADHOC_QUERY_SUCCESS_RESPONSE);
                } else if ("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure".equalsIgnoreCase(from_a0
                        .getResponse().getRegistryResponse().getStatus())
                        || !from_a0.getResponse().getRegistryResponse().getRegistryErrorList().getRegistryError()
                                .isEmpty()) {
                    to_a0.getResponse().getRegistryResponse()
                            .setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
                    if (to_a0.getResponse().getRegistryResponse().getRegistryErrorList() == null) {
                        to_a0.getResponse().getRegistryResponse().setRegistryErrorList(new RegistryErrorList());
                    }
                    to_a0.getResponse()
                            .getRegistryResponse()
                            .getRegistryErrorList()
                            .getRegistryError()
                            .addAll(from_a0.getResponse().getRegistryResponse().getRegistryErrorList()
                                    .getRegistryError());
                }

                if (from_a0.getResponse() != null) {
                    to_a0.getResponse().getDocumentResponse().addAll(from_a0.getResponse().getDocumentResponse());
                } 
            } /*
               * else if (from instanceof EntityDocRetrieveOrchestratableImpl_a1) {
               * 
               * }
               */
        } else {
            // throw error, this aggregator does not handle this case
            getLogger().error("This aggregator only aggregates to EntityDocRetrieveOrchestratableImpl_a0.");
        }
    }

}
