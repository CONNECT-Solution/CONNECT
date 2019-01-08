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
package gov.hhs.fha.nhinc.docsubmission._20.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docsubmission.DocSubmissionUtils;
import gov.hhs.fha.nhinc.docsubmission.outbound.OutboundDocSubmission;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;

import javax.xml.ws.WebServiceContext;

import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EntityDocSubmissionImpl_g1 extends BaseService {

    private static final Logger LOG = LoggerFactory.getLogger(EntityDocSubmissionImpl_g1.class);

    private OutboundDocSubmission outboundDocSubmission;

    public EntityDocSubmissionImpl_g1(OutboundDocSubmission outboundDocSubmission) {
        this.outboundDocSubmission = outboundDocSubmission;
    }

    public RegistryResponseType provideAndRegisterDocumentSetBUnsecured(
            RespondingGatewayProvideAndRegisterDocumentSetRequestType body, WebServiceContext context) {
        return provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), body.getAssertion(),
                body.getNhinTargetCommunities(), body.getUrl());
    }

    public RegistryResponseType provideAndRegisterDocumentSetBSecured(
            RespondingGatewayProvideAndRegisterDocumentSetSecuredRequestType body, WebServiceContext context) {
        AssertionType assertion = getAssertion(context, null);

        return provideAndRegisterDocumentSetB(body.getProvideAndRegisterDocumentSetRequest(), assertion,
                body.getNhinTargetCommunities(), body.getUrl());
    }

    RegistryResponseType provideAndRegisterDocumentSetB(ProvideAndRegisterDocumentSetRequestType request,
            AssertionType assertIn, NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {

        RegistryResponseType response = null;
        try {
            DocSubmissionUtils.getInstance().setTargetCommunitiesVersion(targets, UDDI_SPEC_VERSION.SPEC_2_0);
            response = outboundDocSubmission.provideAndRegisterDocumentSetB(request, assertIn, targets, urlInfo);
        } catch (Exception e) {
            LOG.error("Failed to send request to Nwhin.", e);
        }

        return response;
    }
}
