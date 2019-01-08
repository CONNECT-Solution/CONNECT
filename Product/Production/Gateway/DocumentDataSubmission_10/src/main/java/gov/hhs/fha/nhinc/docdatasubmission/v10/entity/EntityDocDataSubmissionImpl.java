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
package gov.hhs.fha.nhinc.docdatasubmission.v10.entity;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayRegisterDocumentSetRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docdatasubmission.outbound.OutboundDocDataSubmission;
import gov.hhs.fha.nhinc.event.error.ErrorEventException;
import gov.hhs.fha.nhinc.messaging.server.BaseService;
import static gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION.SPEC_1_0;
import gov.hhs.fha.nhinc.util.NhinUtils;
import javax.xml.ws.WebServiceContext;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

class EntityDocDataSubmissionImpl extends BaseService {

    private OutboundDocDataSubmission outboundDocDataSubmission;

    public EntityDocDataSubmissionImpl(OutboundDocDataSubmission outboundDocDataSubmission) {
        this.outboundDocDataSubmission = outboundDocDataSubmission;
    }

    RegistryResponseType registerDocumentSetBUnsecured(RespondingGatewayRegisterDocumentSetRequestType request) {

        RegistryResponseType response = null;

        try {
            NhinUtils.getInstance().setTargetCommunitiesVersion(request.getNhinTargetCommunities(), SPEC_1_0);
            response = outboundDocDataSubmission.registerDocumentSetB(request.getRegisterDocumentSetRequest(),
                request.getAssertion(), request.getNhinTargetCommunities(), request.getUrl());

        } catch (Exception e) {
            throw new ErrorEventException(e, "Unable to call Nhin Doc Data Submission");
        }

        return response;
    }

    RegistryResponseType registerDocumentSetBSecured(RespondingGatewayRegisterDocumentSetSecuredRequestType request,
        WebServiceContext context) {

        RegistryResponseType response = null;

        try {
            AssertionType assertion = getAssertion(context, null);
            NhinUtils.getInstance().setTargetCommunitiesVersion(request.getNhinTargetCommunities(), SPEC_1_0);
            response = outboundDocDataSubmission.registerDocumentSetB(request.getRegisterDocumentSetRequest(),
                assertion, request.getNhinTargetCommunities(), request.getUrl());

        } catch (Exception e) {
            throw new ErrorEventException(e, "Unable to call Nhin Doc Data Submission");
        }

        return response;
    }

}
