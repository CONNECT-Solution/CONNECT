/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.gateway.aggregator.model;

import java.util.Date;
import java.util.Set;

/**
 * This class represents one row of the AGGREGATOR.AGG_TRANSACTION
 * SQL table.
 * 
 * @author Les Westberg
 */
public class AggTransaction
{
    // Private member variables
    //-------------------------
    private String transactionId;
    private String serviceType;
    private Date transactionStartTime;
    private Set<AggMessageResult> aggMessageResults;

    /**
     * Default constructor.
     */
    public AggTransaction()
    {
        clear();
    }
    
    /**
     * Clear the contents of this object and set it to the default state.
     */
    public void clear()
    {
        transactionId = "";
        serviceType = "";
        transactionStartTime = null;
    }

    /**
     * Return the service type of the services that is being called for
     * which results are being aggregated.
     * 
     * @return The name of the service type.
     */
    public String getServiceType()
    {
        return serviceType;
    }

    /**
     * Set the service type of the services that is being called for
     * which results are being aggregated.
     * 
     * @param serviceType The name of the service type.
     */
    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    
    /**
     * Return the transaction ID associated with this aggregation 
     * transaction.
     * 
     * @return The transaction ID for this aggregation transaction.
     */
    public String getTransactionId()
    {
        return transactionId;
    }

    /**
     * Sets the transaction ID associated with this aggregation 
     * transaction.
     * 
     * @param transactionId  The transaction ID for this aggregation transaction.
     */
    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }

    /**
     * Return the time that the transaction was started.
     * 
     * @return The date and time that this transaction was started.
     */
    public Date getTransactionStartTime()
    {
        return transactionStartTime;
    }

    /**
     * Sets the time that the transaction was started.
     * 
     * @param transactionStartTime The date and time that this transaction was started.
     */
    public void setTransactionStartTime(Date transactionStartTime)
    {
        this.transactionStartTime = transactionStartTime;
    }

    /**
     * Return the set of message results associated with this transaction.
     * 
     * @return the set of message results associated with this transaction.
     */
    public Set<AggMessageResult> getAggMessageResults()
    {
        return aggMessageResults;
    }

    /**
     * Sets the set of message results associated with this transaction.
     * 
     * @param aggMessageResults  the set of message results associated with this transaction.
     */
    public void setAggMessageResults(Set<AggMessageResult> aggMessageResults)
    {
        this.aggMessageResults = aggMessageResults;
    }
    
    
}
