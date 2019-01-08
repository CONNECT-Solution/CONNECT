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
package gov.hhs.fha.nhinc.logging.transaction.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author jasonasmith
 *
 */
public class TransactionRepo implements Serializable {

    private static final long serialVersionUID = -4477402717133468043L;
    private Long id;
    private String messageId;
    private String transactionId;
    private Timestamp time;

    /**
     * Get record ID.
     *
     * @return id the record id
     */
    public Long getId() {
        return id;
    }

    /**
     * Set record ID.
     *
     * @param id sets the record id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get message ID.
     *
     * @return messageId the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Set message ID.
     *
     * @param messageId the value for the messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Get transaction ID.
     *
     * @return transactionId the transactionID
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Set transaction ID.
     *
     * @param transactionId value for the transaction Id
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Get timestamp.
     *
     * @return timestamp the timestamp value
     */
    public Timestamp getTime() {
        if (time == null) {
            return null;
        } else {
            return (Timestamp) time.clone();
        }
    }

    /**
     * Set timestamp.
     *
     * @param time the value for the timestamp
     */
    public void setTime(Timestamp time) {
        if (time != null) {
            this.time = (Timestamp) time.clone();
        } else {
            this.time = null;
        }
    }
}
