/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.proxy;

import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.CompleteLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionResponseType;
import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;

/**
 * This interface is used to wrap the GatewayLiftManager within a
 * component proxy object.
 *
 * @author Les Westberg
 */
public interface GatewayLiftManagerProxy
{
    /**
     * This method starts a lift transaction.
     *
     * @param startLiftTransactionRequest The information needed to start a lift transaction.
     * @return The status of the request to start the transaction.  The really only valid answer is "SUCCESS".  Any other situation
     *         should throw an exception.
     */
    public StartLiftTransactionResponseType startLiftTransaction(StartLiftTransactionRequestType startLiftTransactionRequest);


    /**
     * This method tells the Gateway Lift Manager that a lift transaction has been completed successfully.
     *
     * @param completeLiftTransactionRequest The information needed to start a lift transaction.
     * @return The status of the request to start the transaction.  The really only valid answer is "SUCCESS".  Any other situation
     *         would be either an exception or the failedLiftTransaction method should be called.
     */
    public CompleteLiftTransactionResponseType completeLiftTransaction(CompleteLiftTransactionRequestType completeLiftTransactionRequest);

    /**
     * This method tells the Gateway Lift Manager that a lift transaction has failed.
     *
     * @param failedLiftTransactionRequest The information needed to start a lift transaction.
     * @return The status of the request to start the transaction.  The really only valid answer is "SUCCESS".  Any other situation
     *         would be either an exception.
     */
    public FailedLiftTransactionResponseType failedLiftTransaction(FailedLiftTransactionRequestType failedLiftTransactionRequest);
   
}
