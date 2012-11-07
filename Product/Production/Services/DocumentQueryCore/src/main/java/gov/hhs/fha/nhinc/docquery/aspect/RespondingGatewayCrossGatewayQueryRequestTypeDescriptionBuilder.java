/**
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
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
package gov.hhs.fha.nhinc.docquery.aspect;

import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.event.EventContextAccessor;
import gov.hhs.fha.nhinc.event.EventDescription;
import gov.hhs.fha.nhinc.event.EventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.MessageRoutingAccessor;

public class RespondingGatewayCrossGatewayQueryRequestTypeDescriptionBuilder implements EventDescriptionBuilder {

    private AdhocQueryRequestDescriptionBuilder delegate = new AdhocQueryRequestDescriptionBuilder();
    private MessageRoutingAccessor msgRouting;
    private EventContextAccessor msgContext;

    @Override
    public void buildTimeStamp() {
        delegate.buildTimeStamp();
    }

    @Override
    public void buildStatuses() {
        delegate.buildStatuses();
    }

    @Override
    public void buildRespondingHCIDs() {
        delegate.buildRespondingHCIDs();
    }

    @Override
    public void buildPayloadTypes() {
        delegate.buildPayloadTypes();
    }

    @Override
    public void buildPayloadSize() {
        delegate.buildPayloadSize();
    }

    @Override
    public void buildNPI() {
        delegate.buildNPI();
    }

    @Override
    public void buildInitiatingHCID() {
        delegate.buildInitiatingHCID();
    }

    @Override
    public void buildErrorCodes() {
        delegate.buildErrorCodes();
    }

    @Override
    public void buildMessageId() {
        delegate.buildMessageId();
    }

    @Override
    public void buildTransactionId() {
        delegate.buildTransactionId();
    }

    @Override
    public void buildServiceType() {
        delegate.buildServiceType();
    }

    @Override
    public void buildResponseMsgIdList() {
        delegate.buildResponseMsgIdList();
    }

    @Override
    public void buildAction() {
        delegate.buildAction();
    }

    @Override
    public EventDescription getEventDescription() {
        return delegate.getEventDescription();
    }

    @Override
    public void createEventDescription() {
        delegate.createEventDescription();
    }

    @Override
    public void setArguments(Object... arguments) {
        RespondingGatewayCrossGatewayQueryRequestType request = (RespondingGatewayCrossGatewayQueryRequestType) arguments[0];
        delegate.setArguments(request.getAdhocQueryRequest(), request.getAssertion());
    }

    @Override
    public void setReturnValue(Object returnValue) {
        delegate.setReturnValue(returnValue);
    }

    protected AdhocQueryRequestDescriptionBuilder getDelegate() {
        return delegate;
    }

    protected void setDelegate(AdhocQueryRequestDescriptionBuilder delegate) {
        this.delegate = delegate;
        setMsgObjects();
    }

    @Override
    public void setMsgRouting(MessageRoutingAccessor msgRouting) {
        this.msgRouting = msgRouting;
        setMsgObjects();
    }

    @Override
    public void setMsgContext(EventContextAccessor msgContext) {
        this.msgContext = msgContext;
        setMsgObjects();
    }

    private void setMsgObjects() {
        if (delegate != null) {
            delegate.setMsgContext(msgContext);
            delegate.setMsgRouting(msgRouting);
        }
    }
}
