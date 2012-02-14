/*
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
package gov.hhs.fha.nhinc.gateway.aggregator.model;

import java.util.Date;
import java.util.Set;

/**
 * This class represents one row of the AGGREGATOR.AGG_TRANSACTION SQL table.
 * 
 * @author Les Westberg
 */
public class AggTransaction {
    // Private member variables
    // -------------------------
    private String transactionId;
    private String serviceType;
    private Date transactionStartTime;
    private Set<AggMessageResult> aggMessageResults;

    /**
     * Default constructor.
     */
    public AggTransaction() {
        clear();
    }

    /**
     * Clear the contents of this object and set it to the default state.
     */
    public void clear() {
        transactionId = "";
        serviceType = "";
        transactionStartTime = null;
    }

    /**
     * Return the service type of the services that is being called for which results are being aggregated.
     * 
     * @return The name of the service type.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Set the service type of the services that is being called for which results are being aggregated.
     * 
     * @param serviceType The name of the service type.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Return the transaction ID associated with this aggregation transaction.
     * 
     * @return The transaction ID for this aggregation transaction.
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * Sets the transaction ID associated with this aggregation transaction.
     * 
     * @param transactionId The transaction ID for this aggregation transaction.
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Return the time that the transaction was started.
     * 
     * @return The date and time that this transaction was started.
     */
    public Date getTransactionStartTime() {
        return transactionStartTime;
    }

    /**
     * Sets the time that the transaction was started.
     * 
     * @param transactionStartTime The date and time that this transaction was started.
     */
    public void setTransactionStartTime(Date transactionStartTime) {
        this.transactionStartTime = transactionStartTime;
    }

    /**
     * Return the set of message results associated with this transaction.
     * 
     * @return the set of message results associated with this transaction.
     */
    public Set<AggMessageResult> getAggMessageResults() {
        return aggMessageResults;
    }

    /**
     * Sets the set of message results associated with this transaction.
     * 
     * @param aggMessageResults the set of message results associated with this transaction.
     */
    public void setAggMessageResults(Set<AggMessageResult> aggMessageResults) {
        this.aggMessageResults = aggMessageResults;
    }

}
