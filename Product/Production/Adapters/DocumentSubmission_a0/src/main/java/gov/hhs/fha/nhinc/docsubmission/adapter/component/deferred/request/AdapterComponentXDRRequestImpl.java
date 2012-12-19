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
package gov.hhs.fha.nhinc.docsubmission.adapter.component.deferred.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterProvideAndRegisterDocumentSetRequestType;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import javax.xml.ws.WebServiceContext;

import org.apache.log4j.Logger;

/**
 * 
 * @author JHOPPESC
 */
public class AdapterComponentXDRRequestImpl {

    private static final Logger LOG = Logger.getLogger(AdapterComponentXDRRequestImpl.class);

    /**
     * Extract information from the context and place it into the assertion.
     * 
     * @param context The web service context.
     * @param assertion The assertion information.
     */
    private void loadAssertion(WebServiceContext context, AssertionType assertion) {
        // TODO: extract information from context and place it in the assertion.
    }

    /**
     * This method is called by the web service. It calls the orchestration code.
     * 
     * @param body The message payload.
     * @param context The web service context.
     * @return Returns the response from the Adapter patient discovery request.
     */
    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequest(
            AdapterProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        LOG.debug("Entering AdapterComponentXDRRequestImpl.provideAndRegisterDocumentSetBRequest");

        AssertionType assertion = null;
        ProvideAndRegisterDocumentSetRequestType request = null;
        if (body != null) {
            request = body.getProvideAndRegisterDocumentSetRequest();
            assertion = body.getAssertion();
        }

        // Load any information from the web service context into the assertion.
        // ----------------------------------------------------------------------
        loadAssertion(context, assertion);

        AdapterComponentDocSubmissionRequestOrchImpl oOrchestrator = new AdapterComponentDocSubmissionRequestOrchImpl();
        XDRAcknowledgementType response = oOrchestrator.provideAndRegisterDocumentSetBRequest(request, assertion);

        // Send response back to the initiating Gateway
        LOG.debug("Exiting AdapterComponentXDRRequestImpl.provideAndRegisterDocumentSetBRequest");
        return response;
    }
}
