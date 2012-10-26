/**
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permittntefed provided that the following conditions are met:
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

import java.util.List;

public abstract class BaseEventDescriptionBuilder implements EventDescriptionBuilder {

    private BaseEventDescription description;
    private MessageRoutingAccessor msgRouting;
    private EventContextAccessor msgContext;

    public BaseEventDescriptionBuilder() {

    }

    public BaseEventDescriptionBuilder(MessageRoutingAccessor msgRouting, EventContextAccessor msgContext) {
        this.msgRouting = msgRouting;
        this.msgContext = msgContext;
    }

    public void setMsgRouting(MessageRoutingAccessor msgRouting) {
        this.msgRouting = msgRouting;
    }

    public void setMsgContext(EventContextAccessor msgContext) {
        this.msgContext = msgContext;
    }

    public EventDescription getEventDescription() {
        return description;
    }

    public void createEventDescription() {
        description = new BaseEventDescription();
    }

    final public void buildMessageId() {
        description.setMessageId(msgRouting.getMessageId());

    }

    final public void buildTransactionId() {
        description.setTransactionId(msgRouting.getTransactionId());
    }

    final public void buildResponseMsgIdList() {
        description.setResponseMsgids(msgRouting.getResponseMsgIdList());
    }

    final public void buildAction() {
        description.setAction(msgContext.getAction());
    }

    final public void buildServiceType() {
        description.setServiceType(msgContext.getServiceType());
    }

    protected void setErrorCodes(List<String> errorCodes) {
        description.setErrorCodes(errorCodes);
    }

    protected void setStatus(String status) {
        description.setStatus(status);
    }

    protected void setNpi(String npi) {
        description.setNpi(npi);
    }

    protected void setRespondingHCIDs(List<String> respondingHCIDs) {
        description.setRespondingHCID(respondingHCIDs);
    }

    protected void setInitiatingHCID(String initiatingHCID) {
        description.setInitiatingHCID(initiatingHCID);
    }

    protected void setPayloadSize(String payloadSize) {
        description.setPayloadSize(payloadSize);
    }

    protected void setPayLoadType(String payLoadType) {
        description.setPayLoadType(payLoadType);
    }

    protected void setTimeStamp(String timeStamp) {
        description.setTimeStamp(timeStamp);
    }

}
