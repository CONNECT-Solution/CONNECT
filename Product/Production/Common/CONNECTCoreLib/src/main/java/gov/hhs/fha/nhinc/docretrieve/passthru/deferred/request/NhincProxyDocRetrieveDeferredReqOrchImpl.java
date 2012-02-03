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
package gov.hhs.fha.nhinc.docretrieve.passthru.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy.NhinDocRetrieveDeferredReqProxy;
import gov.hhs.fha.nhinc.docretrieve.nhin.deferred.request.proxy.NhinDocRetrieveDeferredReqProxyObjectFactory;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Passthru Proxy Orchestration Request implementation class
 *
 * @author Sai Valluripalli
 * @author richard.ettema
 */
public class NhincProxyDocRetrieveDeferredReqOrchImpl {

    //Logger
    private static final Log log = LogFactory.getLog(NhincProxyDocRetrieveDeferredReqOrchImpl.class);

    /**
     * Pass the processing on to the NHIN layer
     *
     * @param request
     * @param assertion
     * @param target
     * @return DocRetrieveAcknowledgementType
     */
    public DocRetrieveAcknowledgementType crossGatewayRetrieveRequest(RetrieveDocumentSetRequestType request, AssertionType assertion, NhinTargetSystemType target) {
        log.debug("Begin NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...)");

        DocRetrieveAcknowledgementType ack = null;

        //Call Nhin component proxy
        NhinDocRetrieveDeferredReqProxyObjectFactory objFactory = new NhinDocRetrieveDeferredReqProxyObjectFactory();
        NhinDocRetrieveDeferredReqProxy docRetrieveProxy = objFactory.getNhinDocRetrieveDeferredRequestProxy();
        ack = docRetrieveProxy.sendToRespondingGateway(request, assertion, target);

        log.debug("End NhincProxyDocRetrieveDeferredReqOrchImpl.crossGatewayRetrieveRequest(...)");

        return ack;
    }

}
