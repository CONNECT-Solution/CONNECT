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
package gov.hhs.fha.nhinc.event;

import org.apache.commons.lang.StringUtils;

/**
 * EventDescriptionBuilder that delegates all work to the other builder. Concrete implementations should call
 * <code>setDelegate</code> during construction.
 *
 * <p>
 * All of the build* methods are final. They each delegate to the builder passed in via setDelegate.
 */
public abstract class DelegatingEventDescriptionBuilder implements EventDescriptionBuilder {

    private EventDescriptionBuilder delegate;
    private MessageRoutingAccessor msgRouting;
    private EventContextAccessor msgContext;

    @Override
    public final void buildTimeStamp() {
        delegate.buildTimeStamp();
    }

    @Override
    public final void buildStatuses() {
        delegate.buildStatuses();
    }

    @Override
    public final void buildRespondingHCIDs() {
        delegate.buildRespondingHCIDs();
    }

    @Override
    public final void buildPayloadTypes() {
        delegate.buildPayloadTypes();
    }

    @Override
    public final void buildPayloadSizes() {
        delegate.buildPayloadSizes();
    }

    @Override
    public final void buildNPI() {
        delegate.buildNPI();
    }

    @Override
    public final void buildInitiatingHCID() {
        delegate.buildInitiatingHCID();
    }

    @Override
    public final void buildErrorCodes() {
        delegate.buildErrorCodes();
    }

    @Override
    public final void buildMessageId() {
        delegate.buildMessageId();
    }

    @Override
    public final void buildTransactionId() {
        delegate.buildTransactionId();
    }

    @Override
    public final void buildServiceType() {
        delegate.buildServiceType();
    }

    @Override
    public final void buildResponseMsgIdList() {
        delegate.buildResponseMsgIdList();
    }

    @Override
    public final void buildVersion() {
        delegate.buildVersion();
    }

    @Override
    public final EventDescription getEventDescription() {
        return delegate.getEventDescription();
    }

    @Override
    public final void createEventDescription() {
        delegate.createEventDescription();
    }

    /**
     * For testing purposes.
     *
     * @return the delegate set by a previous call to <code>setDelegate</code>.
     */
    public final EventDescriptionBuilder getDelegate() {
        return delegate;
    }

    /**
     * @param delegate The builder to delegate to, after transforming the arguments. Must not be null.
     */
    protected final void setDelegate(EventDescriptionBuilder delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate cannot be null");
        }
        this.delegate = delegate;
        setMsgObjects();
    }

    @Override
    public final void setMsgRouting(MessageRoutingAccessor msgRouting) {
        this.msgRouting = msgRouting;
        setMsgObjects();
    }

    @Override
    public final void setMsgContext(EventContextAccessor msgContext) {
        this.msgContext = msgContext;
        setMsgObjects();
    }

    private void setMsgObjects() {
        if (delegate != null) {
            delegate.setMsgContext(msgContext);
            delegate.setMsgRouting(msgRouting);
        }
    }

    @Override
    public final String getServiceType() {
        return delegate.getServiceType();
    }

    @Override
    public final String getInitiatorHcid() {
        return delegate.getInitiatorHcid();
    }

    @Override
    public final String getRespondingHcid() {
        return delegate.getRespondingHcid();
    }

    @Override
    public final void buildMessageId(String messageId) {
        if (StringUtils.isNotEmpty(messageId)) {
            delegate.buildMessageId(messageId);
        } else {
            delegate.buildMessageId();
        }
    }
}
