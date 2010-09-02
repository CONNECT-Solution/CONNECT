/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.lift.common.util.cleanup;

import gov.hhs.fha.nhinc.gateway.lift.FailedLiftTransactionRequestType;
import gov.hhs.fha.nhinc.gateway.lift.StartLiftTransactionRequestType;
import gov.hhs.fha.nhinc.lift.dao.GatewayLiftMessageDao;
import gov.hhs.fha.nhinc.lift.model.GatewayLiftMsgRecord;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxy;
import gov.hhs.fha.nhinc.lift.proxy.GatewayLiftManagerProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility to clean up messages that have stalled while processing. Messages that have been entered but not started
 * are re-started and messages that have stalled while processing will be moved to a stale state.
 *
 * This is a singleton and instances must be obtained by the static "getInstance()" method.
 * 
 * @author Neil Webb
 */
public class CleanupUtil
{
    private static final String PROPERTIES_FILE_GATEWAY = "gateway";
    private static final String PROPERTY_KEY_CLEANUP_INTERVAL_MINUTES = "LiftMessageCleanupIntervalMinutes";
    private static final String PROPERTY_KEY_STALE_MESSAGE_ENTERED_MINUTES = "LiftStaleMessageDurationEnteredDuration";
    private static final String PROPERTY_KEY_STALE_MESSAGE_PROCESSING_MINUTES = "LiftStaleMessageDurationProcessingMinutes";
    private static final String FAILED_TRANSACTION_MESSAGE = "Message processing timed out.";

    private Log log = null;
    private static boolean isRunning = false;
    private static Date lastCleanup = null;

    private static CleanupUtil _instance = null;

    protected CleanupUtil()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }
	
	protected static CleanupUtil createInstance()
    {
        return new CleanupUtil();
    }

    /**
     * Retrieve the only instance of this class.
     * 
     * @return CleanupUtil instance
     */
    public static CleanupUtil getInstance()
    {
        if(_instance == null)
        {
            _instance = createInstance();
        }
        return _instance;
    }

    /**
     * Cleanup stale records that are in a "entered" or "processing" state.
     */
    public synchronized void cleanupRecords()
    {
        if(isCleanupRequired())
        {
            isRunning = true;
            log.info("cleanupRecords - cleanup required, restarting processing of stale records.");
            restartStaleEnteredRecords();
            restartStaleProcessingRecords();
            lastCleanup = new Date();
            isRunning = false;
        }
        else
        {
            log.info("cleanupRecords - no cleanup required.");
        }
    }

    /**
     * Get the time that the last cleanup was performed.
     *
     * @return Last cleanup timestamp
     */
    protected Date getLastCleanup()
    {
        return lastCleanup;
    }

    /**
     * Get flag to indicate if the cleanup utility is running.
     *
     * @return Running flag
     */
    protected boolean getIsRunningFlag()
    {
        log.debug("Returning isRunning flag value: " + isRunning);
        return isRunning;
    }

    /**
     * Determine if cleanup is required.
     *
     * @return Flag to indicate if cleanup is required.
     */
    protected boolean isCleanupRequired()
    {
        boolean cleanupRequired = false;
        try
        {
            if(getIsRunningFlag())
            {
                cleanupRequired = false;
                log.debug("isCleanupRequired: already runnning - returning false");
            }
            else if((getLastCleanup() != null))
            {
                Calendar workingTarget = GregorianCalendar.getInstance();
                workingTarget.setTime(getLastCleanup());
                workingTarget.add(Calendar.MINUTE, getCleanupIntervalMinutes());
                cleanupRequired = workingTarget.getTime().after(getCurrentDate());
                log.debug("isCleanupRequired: calculated cleanup interval - returning: " + cleanupRequired);
            }
            else
            {
                cleanupRequired = true;
                log.debug("isCleanupRequired: last cleanup was null - returning true");
            }
        }
        catch(Throwable t)
        {
            cleanupRequired = true;
            log.error("Exception encountered while determining if cleanup is required: " + t.getMessage(), t);
        }
        return cleanupRequired;
    }

    protected Date getCurrentDate()
    {
        return new Date();
    }

    protected long getCleanupIntervalProperty() throws PropertyAccessException
    {
        return PropertyAccessor.getPropertyLong(PROPERTIES_FILE_GATEWAY, PROPERTY_KEY_CLEANUP_INTERVAL_MINUTES);
    }

    /**
     * Get the interval in minutes of how often the cleanup process should run. This is the minimum delay between runs.
     * 
     * @return Minimum cleanup interval in minutes
     */
    protected int getCleanupIntervalMinutes()
    {
        int cleanupIntervalMinutes = 0;
        try
        {
           cleanupIntervalMinutes = (int)getCleanupIntervalProperty();
        }
        catch(PropertyAccessException pae)
        {
            log.error("Error retrieving cleanup interval property: " + pae.getMessage(), pae);
        }
        log.debug("getCleanupIntervalMinutes - returning: " + cleanupIntervalMinutes);
        return cleanupIntervalMinutes;
    }

    protected long getStaleMessageEnteredProperty() throws PropertyAccessException
    {
        return PropertyAccessor.getPropertyLong(PROPERTIES_FILE_GATEWAY, PROPERTY_KEY_STALE_MESSAGE_ENTERED_MINUTES);
    }

    /**
     * Get the amount of time in minutes that is used to determine if a message that is in the "entered" status is
     * stale. If that number of minutes has expired since the message entered the status, it will be determined that
     * the message has failed to start processing and needs to be restarted.
     *
     * @return Number of minutes for a message in the entered status to be considered stale.
     */
    protected int getStaleMessageEnteredMinutes()
    {
        int staleMessageEnteredMinutes = 0;
        try
        {
           staleMessageEnteredMinutes = (int)getStaleMessageEnteredProperty();
        }
        catch(PropertyAccessException pae)
        {
            log.error("Error retrieving stale message entered property: " + pae.getMessage(), pae);
        }
        log.debug("getStaleMessageEnteredMinutes - returning: " + staleMessageEnteredMinutes);
        return staleMessageEnteredMinutes;
    }

    protected long getStaleMessageProcessingProperty() throws PropertyAccessException
    {
        return PropertyAccessor.getPropertyLong(PROPERTIES_FILE_GATEWAY, PROPERTY_KEY_STALE_MESSAGE_PROCESSING_MINUTES);
    }

    /**
     * Get the amount of time in minutes that is used to determine if a message that is in the "processing" status is
     * stale. If that number of minutes has expired since the message entered the status, it will be determined that
     * the message has failed to complete processing and has failed.
     *
     * @return Number of minutes for a message in the processing status to be considered stale.
     */
    protected int getStaleMessageProcessingMinutes()
    {
        int staleMessageProcessingMinutes = 0;
        try
        {
           staleMessageProcessingMinutes = (int)getStaleMessageProcessingProperty();
        }
        catch(PropertyAccessException pae)
        {
            log.error("Error retrieving stale message processing property: " + pae.getMessage(), pae);
        }
        log.debug("getStaleMessageProcessingMinutes - returning: " + staleMessageProcessingMinutes);
        return staleMessageProcessingMinutes;
    }

    /**
     * Restart stale messages that are in the "entered" state.
     */
    protected void restartStaleEnteredRecords()
    {
        try
        {
            int staleMessageEnteredOffset = getStaleMessageEnteredMinutes();
            if(staleMessageEnteredOffset < 1)
            {
                log.warn("Invalid stale message entered minutes value (" + staleMessageEnteredOffset + ") - not restarting stale messages in entered status");
                return;
            }
            Date staleEnteredDate = createStaleDate(staleMessageEnteredOffset);
            List<GatewayLiftMsgRecord> staleRecords = getStaleRecords(NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED, staleEnteredDate);
            if(staleRecords == null)
            {
                log.info("Stale entered message list was null.");
            }
            else
            {
                log.info("Stale message list for entered status has " + staleRecords.size() + " record(s).");
                for(GatewayLiftMsgRecord record : staleRecords)
                {
                    log.info("Restarting processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_ENTERED + " state with request key guid: " + record.getRequestKeyGuid());
                    StartLiftTransactionRequestType startLiftTransactionRequest = new StartLiftTransactionRequestType();
                    startLiftTransactionRequest.setRequestKeyGuid(record.getRequestKeyGuid());
                    getGatewayLiftManagerProxy().startLiftTransaction(startLiftTransactionRequest);
                }
            }
        }
        catch(Throwable t)
        {
            log.error("Exception encountered while processing stale records in entered status: " + t.getMessage(), t);
        }
    }

    /**
     * Fail stale messages that are in the "processing" state.
     */
    protected void restartStaleProcessingRecords()
    {
        try
        {
            int staleMessageProcessingOffset = getStaleMessageProcessingMinutes();
            if(staleMessageProcessingOffset < 1)
            {
                log.warn("Invalid stale message processing minutes value (" + staleMessageProcessingOffset + ") - not restarting stale messages in processing status.");
                return;
            }
            Date staleProcessingDate = createStaleDate(staleMessageProcessingOffset);
            List<GatewayLiftMsgRecord> staleRecords = getStaleRecords(NhincConstants.LIFT_TRANSFER_DB_STATE_PROCESSING, staleProcessingDate);
            if(staleRecords == null)
            {
                log.info("Stale processing message list was null.");
            }
            else
            {
                log.info("Stale message list for processing status has " + staleRecords.size() + " record(s).");
                for(GatewayLiftMsgRecord record : staleRecords)
                {
                    log.info("Failing processing for message in " + NhincConstants.LIFT_TRANSFER_DB_STATE_PROCESSING + " state with request key guid: " + record.getRequestKeyGuid());
                    FailedLiftTransactionRequestType failedLiftTransactionRequest = new FailedLiftTransactionRequestType();
                    failedLiftTransactionRequest.setRequestKeyGuid(record.getRequestKeyGuid());
                    failedLiftTransactionRequest.setErrorMessage(FAILED_TRANSACTION_MESSAGE);
                    getGatewayLiftManagerProxy().failedLiftTransaction(failedLiftTransactionRequest);
                }
            }
        }
        catch(Throwable t)
        {
            log.error("Exception encountered while processing stale records in processing status: " + t.getMessage(), t);
        }
    }

    /**
     * Retrieve stale messages. Messages in the state provided older than the date provided will be retrieved.
     *
     * @param messageStatus Status of the messages to retrieve
     * @param staleMessageDate The most recent date to use as a cutoff.
     *
     * @return List of stale messages.
     */
    protected List<GatewayLiftMsgRecord> getStaleRecords(String messageStatus, Date staleMessageDate)
    {
        List<GatewayLiftMsgRecord> staleRecords = null;

        if((messageStatus == null) || (messageStatus.length() < 1))
        {
            log.error("Message status was null or empty.");
        }
        else if(staleMessageDate == null)
        {
            log.error("Stale message date was null.");
        }
        else
        {
            GatewayLiftMessageDao messageDao = getGatewayLiftMessageDao();
            if(messageDao == null)
            {
                log.error("GatewayLiftMessageDao was null.");
            }
            else
            {
                staleRecords = messageDao.queryByMessageTypeOrderByProcessingTime(messageStatus, staleMessageDate);
            }
        }
        return staleRecords;
    }

    protected GatewayLiftMessageDao getGatewayLiftMessageDao()
    {
        return new GatewayLiftMessageDao();
    }

    protected Date createStaleDate(int minuteOffset)
    {
        Calendar staleDate = GregorianCalendar.getInstance();
        staleDate.setTime(getCurrentDate());
        staleDate.add(Calendar.MINUTE, -minuteOffset);
        return staleDate.getTime();
    }

    protected GatewayLiftManagerProxy getGatewayLiftManagerProxy()
    {
        return new GatewayLiftManagerProxyObjectFactory().getGatewayLiftManagerProxy();
    }

}
