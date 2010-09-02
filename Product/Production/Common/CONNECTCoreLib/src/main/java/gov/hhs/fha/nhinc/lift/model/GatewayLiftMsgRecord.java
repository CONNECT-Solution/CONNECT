/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.model;

import java.sql.Blob;
import java.util.Date;

/**
 * This class represents a row in the lift.gateway_lift_message table.
 *
 * @author Les Westberg
 */
public class GatewayLiftMsgRecord
{
    private Long id = null;
    private Date initialEntryTimestamp = null;
    private String messageState = null;
    private Date processingStartTimestamp = null;
    private String producerProxyAddress = null;
    private Long producerProxyPort = null;
    private String fileNameToRetrieve = null;
    private String requestKeyGuid = null;
    private String messageType = null;
    private Blob message = null;
    private Blob assertion = null;

    /**
     * Default constructor.
     */
    public GatewayLiftMsgRecord()
    {
    }

    /**
     * Return the assertion information.
     *
     * @return The assertion information.
     */
    public Blob getAssertion()
    {
        return assertion;
    }

    /**
     * Set the assertion information.
     *
     * @param assertion The assertion information.
     */
    public void setAssertion(Blob assertion)
    {
        this.assertion = assertion;
    }

    /**
     * Return the name of the file that is being retrieved.
     *
     * @return The name of the file that is being retrieved.
     */
    public String getFileNameToRetrieve()
    {
        return fileNameToRetrieve;
    }

    /**
     * Set the name of the file that is being retrieved.
     *
     * @param fileNameToRetrieve The name of the file that is being retrieved.
     */
    public void setFileNameToRetrieve(String fileNameToRetrieve)
    {
        this.fileNameToRetrieve = fileNameToRetrieve;
    }

    /**
     * Return the unique ID of the record.
     *
     * @return The unique ID of the record.
     */
    public Long getId()
    {
        return id;
    }

    /**
     * Set the unique ID of the record.
     *
     * @param id The unique ID of the record.
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Returns the time stamp when the record has been entered into the table.
     * 
     * @return The time stamp when the record has been entered into the table.
     */
    public Date getInitialEntryTimestamp()
    {
        return initialEntryTimestamp;
    }

    /**
     * Sets the time stamp when the record has been entered into the table.
     *
     * @param initialEntryTimestamp The time stamp when the record has been entered into the table.
     */
    public void setInitialEntryTimestamp(Date initialEntryTimestamp)
    {
        this.initialEntryTimestamp = initialEntryTimestamp;
    }

    /**
     * Returns the NHIN message.
     *
     * @return The NHIN message.
     */
    public Blob getMessage()
    {
        return message;
    }

    /**
     * Sets the NHIN message.
     *
     * @param message The NHIN message.
     */
    public void setMessage(Blob message)
    {
        this.message = message;
    }

    /**
     * Returns the state of the message.  Current valid values are: ENTERED and PROCESSING.
     *
     * @return The state of the message.  Current valid values are: ENTERED and PROCESSING.
     */
    public String getMessageState()
    {
        return messageState;
    }

    /**
     * Sets the state of the message.  Current valid values are: ENTERED and PROCESSING.
     *
     * @param messageState The state of the message.  Current valid values are: ENTERED and PROCESSING.
     */
    public void setMessageState(String messageState)
    {
        this.messageState = messageState;
    }

    /**
     * Returns the type of message.  Current valid values are: 'DEFERRED_DOCUMENT_SUBMISSION'.
     *
     * @return The type of message.  Current valid values are: 'DEFERRED_DOCUMENT_SUBMISSION'.
     */
    public String getMessageType()
    {
        return messageType;
    }

    /**
     * Sets the type of message.  Current valid values are: 'DEFERRED_DOCUMENT_SUBMISSION'.
     *
     * @param messageType The type of message.  Current valid values are: 'DEFERRED_DOCUMENT_SUBMISSION'.
     */
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    /**
     * Returns the timestamp of the time when processing was started.
     *
     * @return The timestamp of the time when processing was started.
     */
    public Date getProcessingStartTimestamp()
    {
        return processingStartTimestamp;
    }

    /**
     * Sets the timestamp of the time when processing was started.
     *
     * @param processingStartTimestamp The timestamp of the time when processing was started.
     */
    public void setProcessingStartTimestamp(Date processingStartTimestamp)
    {
        this.processingStartTimestamp = processingStartTimestamp;
    }

    /**
     * Returns the producer proxy address.
     *
     * @return The producer proxy address.
     */
    public String getProducerProxyAddress()
    {
        return producerProxyAddress;
    }

    /**
     * Sets the producer proxy address.
     *
     * @param producerProxyAddress The producer proxy address.
     */
    public void setProducerProxyAddress(String producerProxyAddress)
    {
        this.producerProxyAddress = producerProxyAddress;
    }

    /**
     * Returns the port number for the transfer.
     *
     * @return The port number for the transfer.
     */
    public Long getProducerProxyPort()
    {
        return producerProxyPort;
    }

    /**
     * Sets the port number for the transfer.
     *
     * @param producerProxyPort The port number for the transfer.
     */
    public void setProducerProxyPort(Long producerProxyPort)
    {
        this.producerProxyPort = producerProxyPort;
    }

    /**
     * Returns the request key GUID.  This is a unique key that was generated for this transfer.
     *
     * @return The request key GUID.  This is a unique key that was generated for this transfer.
     */
    public String getRequestKeyGuid()
    {
        return requestKeyGuid;
    }

    /**
     * Sets the request key GUID.  This is a unique key that was generated for this transfer.
     *
     * @param requestKeyGuid The request key GUID.  This is a unique key that was generated for this transfer.
     */
    public void setRequestKeyGuid(String requestKeyGuid)
    {
        this.requestKeyGuid = requestKeyGuid;
    }

}
