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
package gov.hhs.fha.nhinc.docretrieve._20.passthru;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.docretrieve._20.ResponseScrubber;
import gov.hhs.fha.nhinc.docretrieve.passthru.NhincProxyDocRetrieveOrchImpl;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;

import javax.xml.ws.WebServiceContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * 
 * @author Neil Webb
 */
public class NhincProxyDocRetrieveImpl extends BaseService {

    private NhincProxyDocRetrieveOrchImpl orchImpl;
    
    private static Log log = LogFactory.getLog(NhincProxyDocRetrieveImpl.class);

    public NhincProxyDocRetrieveImpl(NhincProxyDocRetrieveOrchImpl orchImpl) {
        this.orchImpl = orchImpl;
    }
    
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, WebServiceContext context) {
        log.debug("NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve(secured)");

        RetrieveDocumentSetResponseType response = null;

        if (body != null) {
            AssertionType assertion = getAssertion(context, null);
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion,
                    body.getNhinTargetSystem());
        }

        ResponseScrubber rs = new ResponseScrubber();
        return rs.Scrub(response);
    }

    public RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(
            RespondingGatewayCrossGatewayRetrieveRequestType body, WebServiceContext context) {
        log.debug("NhincProxyDocRetrieveImpl.respondingGatewayCrossGatewayRetrieve(unsecured)");

        RetrieveDocumentSetResponseType response = null;

        if (body != null) {
            AssertionType assertion = getAssertion(context, body.getAssertion());
            response = orchImpl.respondingGatewayCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest(), assertion,
                    body.getNhinTargetSystem());
        }
        ResponseScrubber rs = new ResponseScrubber();
        return rs.Scrub(response);
    }
}
