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
package gov.hhs.fha.nhinc.event.model;

import gov.hhs.fha.nhinc.event.Event;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Modeled Event for Hibernate Mapping.
 */
public class DatabaseEvent implements Event {

    private Long id;

    private String messageID;
    private String transactionID;
    private String description;
    private String serviceType;
    private String initiatorHcid;
    private String respondingHcid;
    private String eventName;
    private Date eventTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getMessageID() {
        return messageID;
    }

    @Override
    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    @Override
    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    @Override
    public String getServiceType() {
        return serviceType;
    }

    @Override
    public void setInitiatorHcid(String hcid) {
        initiatorHcid = hcid;
    }

    @Override
    public String getInitiatorHcid() {
        return initiatorHcid;
    }

    @Override
    public void setRespondingHcid(String hcid) {
        respondingHcid = hcid;
    }

    @Override
    public String getRespondingHcid() {
        return respondingHcid;
    }

    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }

    public String getFormattedEventTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm");
        return sdf.format(eventTime);
    }

}
