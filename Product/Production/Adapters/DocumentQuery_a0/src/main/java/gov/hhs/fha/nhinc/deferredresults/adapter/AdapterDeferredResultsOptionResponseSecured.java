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
package gov.hhs.fha.nhinc.deferredresults.adapter;

import gov.hhs.fha.nhinc.common.nhinccommonadapter.AdapterDeferredResultsResponseType;
import gov.hhs.fha.nhinc.deferredresults.impl.AdapterDeferredResultsOption;
import gov.hhs.fha.nhinc.dq.adapterdeferredresultoptionsecured.AdapterDocQueryDeferredResultsOptionSecuredPortType;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Adapter webservice to process deferred documents into a deferred response option message to send back to the
 * Initiating Gateway at the endpoint specified in the initial request.
 */
@Service
public class AdapterDeferredResultsOptionResponseSecured extends BaseService implements
AdapterDocQueryDeferredResultsOptionSecuredPortType {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterDeferredResultsOptionResponseSecured.class);

    @Autowired
    AdapterDeferredResultsOption adapterImpl;

    @Resource
    private WebServiceContext context;

    @Override
    public AdapterDeferredResultsResponseType respondingGatewayCrossGatewayQueryDeferredResultsSecured(
        AdhocQueryResponse message) {
        LOG.info("Running through Adapter Secured layer.");
        return adapterImpl.respondingGatewayCrossGatewayQueryResults(message, getAssertion(context));
    }

}