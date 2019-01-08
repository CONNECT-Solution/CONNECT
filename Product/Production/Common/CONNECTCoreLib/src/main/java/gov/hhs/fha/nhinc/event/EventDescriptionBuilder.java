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

public interface EventDescriptionBuilder {

    void buildMessageId();

    void buildTransactionId();

    void buildTimeStamp();

    void buildStatuses();

    void buildServiceType();

    void buildResponseMsgIdList();

    void buildRespondingHCIDs();

    void buildPayloadTypes();

    void buildPayloadSizes();

    void buildNPI();

    void buildInitiatingHCID();

    void buildErrorCodes();

    void buildVersion();

    EventDescription getEventDescription();

    void createEventDescription();

    /**
     * Intended to take the arguments from the method that was executed when this event was triggered and to be used to
     * build the event description. Needs to be implemented in sub classes because only the subs will know what objects
     * they can operate on.
     *
     * @param arguments
     */
    void setArguments(Object... arguments);

    /**
     * Intended to take the return value from the method that was executed when this event was triggered and to be used
     * to build the event description. Needs to be implemented in sub classes because only the subs will know what
     * objects they can operate on.
     *
     * The returnValue will only have a valid object if the event was executed after returning from a method. It will be
     * null otherwise.
     *
     * @param returnValue
     */
    void setReturnValue(Object returnValue);

    void setMsgRouting(MessageRoutingAccessor msgRouting);

    void setMsgContext(EventContextAccessor msgContext);

    String getServiceType();

    String getInitiatorHcid();

    String getRespondingHcid();

    void buildMessageId(String messageId);
}
