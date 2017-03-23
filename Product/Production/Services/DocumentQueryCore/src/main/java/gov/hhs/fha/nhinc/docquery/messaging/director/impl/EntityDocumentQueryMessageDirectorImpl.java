/*
 * Copyright (c) 2009-2017, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docquery.messaging.director.impl;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.docquery.builder.AdhocQueryRequestBuilder;
import gov.hhs.fha.nhinc.docquery.messaging.director.EntityDocumentQueryMessageDirector;
import gov.hhs.fha.nhinc.messaging.director.AbstractMessageDirector;

/**
 *
 * @author tjafri
 */
public class EntityDocumentQueryMessageDirectorImpl extends AbstractMessageDirector implements
    EntityDocumentQueryMessageDirector {

    /**
     * The message.
     */
    private RespondingGatewayCrossGatewayQueryRequestType message = null;

    /**
     * The dq builder.
     */
    private AdhocQueryRequestBuilder dqBuilder = null;

    @Override
    public RespondingGatewayCrossGatewayQueryRequestType getMessage() {
        return message;
    }

    @Override
    public void build() {
        message = new RespondingGatewayCrossGatewayQueryRequestType();

        if (dqBuilder != null) {
            dqBuilder.build();
            message.setAdhocQueryRequest(dqBuilder.getMessage());
        }

        if (assertionBuilder != null) {
            assertionBuilder.build();
            message.setAssertion(assertionBuilder.getAssertion());
        }

        if (targetBuilder != null) {
            targetBuilder.build();
            message.setNhinTargetCommunities(targetBuilder.getNhinTargetCommunities());
        }
    }

    @Override
    public void setDocumentQueryBuilder(AdhocQueryRequestBuilder dqBuilder) {
        this.dqBuilder = dqBuilder;
    }

}
