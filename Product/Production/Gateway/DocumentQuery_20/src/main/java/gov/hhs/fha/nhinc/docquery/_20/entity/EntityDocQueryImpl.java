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
package gov.hhs.fha.nhinc.docquery._20.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.cxf.extraction.SAML2AssertionExtractor;
import gov.hhs.fha.nhinc.docquery.entity.EntityDocQueryOrchImpl;
import gov.hhs.fha.nhinc.gateway.servlet.InitServlet;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


class EntityDocQueryImpl {

    private Log log = null;
    private EntityDocQueryOrchImpl orchImpl;

    public EntityDocQueryImpl(EntityDocQueryOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    
    public AdhocQueryResponse respondingGatewayCrossGatewayQuerySecured(RespondingGatewayCrossGatewayQuerySecuredRequestType request, WebServiceContext context) {
        log.info("Begin respondingGatewayCrossGatewayQuerySecured(RespondingGatewayCrossGatewayQuerySecuredRequestType, WebServiceContext)");
        EntityDocQueryOrchImpl implOrch = createEntityDocQueryOrchImpl();
        AdhocQueryResponse response = null;

        try {
            if (request != null) {
                AdhocQueryRequest adhocQueryRequest = request.getAdhocQueryRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                response = implOrch.respondingGatewayCrossGatewayQuery( adhocQueryRequest, SAML2AssertionExtractor.getInstance().extractSamlAssertion(context), targets);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass()
                        + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error(
                    "Failed to call the web orchestration (" + implOrch.getClass()
                    + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    AdhocQueryResponse respondingGatewayCrossGatewayQueryUnsecured(
            RespondingGatewayCrossGatewayQueryRequestType request, WebServiceContext context) {
        log.info("Begin respondingGatewayCrossGatewayQueryUnsecured(RespondingGatewayCrossGatewayQuerySecuredRequestType, WebServiceContext)");
        EntityDocQueryOrchImpl implOrch = createEntityDocQueryOrchImpl();
        AdhocQueryResponse response = null;

        try {
            if (request != null) {
                AdhocQueryRequest adhocQueryRequest = request.getAdhocQueryRequest();
                NhinTargetCommunitiesType targets = request.getNhinTargetCommunities();
                AssertionType assertIn = request.getAssertion();
                response = implOrch.respondingGatewayCrossGatewayQuery( adhocQueryRequest, assertIn, targets);
            } else {
                log.error("Failed to call the web orchestration (" + implOrch.getClass()
                        + ".respondingGatewayCrossGatewayQuery).  The input parameter is null.");
            }
        } catch (Exception e) {
            log.error(
                    "Failed to call the web orchestration (" + implOrch.getClass()
                    + ".respondingGatewayCrossGatewayQuery).  An unexpected exception occurred.  "
                    + "Exception: " + e.getMessage(), e);
        }
        return response;
    }

    private EntityDocQueryOrchImpl createEntityDocQueryOrchImpl() {
        orchImpl.setExecutorService(InitServlet.getExecutorService(), InitServlet.getLargeJobExecutorService());
        
        return orchImpl;
    }
}
