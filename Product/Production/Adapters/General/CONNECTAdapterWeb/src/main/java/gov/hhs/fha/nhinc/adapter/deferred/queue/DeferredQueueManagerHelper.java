/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.SuccessOrFailType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue.EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Helper class for the web service.
 *
 * @author richard.ettema
 */
public class DeferredQueueManagerHelper {

    private static Log log = LogFactory.getLog(DeferredQueueManagerHelper.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String DEFERRED_QUEUE_GLOBAL_THRESHOLD = "DeferredQueueGlobalThreshold";
    private static final int DEFERRED_QUEUE_GLOBAL_THRESHOLD_DEFAULT = 100;
    private int iGlobalThreshold = DEFERRED_QUEUE_GLOBAL_THRESHOLD_DEFAULT;

    /**
     * Called via web service interface
     * @param deferredQueueManagerForceProcessRequest
     * @return
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredQueue(DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        DeferredQueueManagerForceProcessResponseType oResponse = new DeferredQueueManagerForceProcessResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            forceProcess();
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to process the Deferred Queue.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Force the deferred queue process
     * @throws DeferredQueueException
     */
    public void forceProcess() throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.forceProcess method - processing deferred queue.");

        setGlobalThreshold();

        // Retrieve AsyncMsgs records that are received and not processed
        log.debug("***** Retrieve queue message records that are received and not processed *****");
        AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();
        List<AsyncMsgRecord> queueRecords = queueDao.queryForDeferredQueueProcessing();

        if (NullChecker.isNotNullish(queueRecords) && queueRecords.size() > 0) {
            int count = 0;
            int maxCount = (queueRecords.size() > iGlobalThreshold ? iGlobalThreshold : queueRecords.size());
            log.debug("***** Found " + queueRecords.size() + " queue message records; will process " + maxCount + " records. *****");

            for (AsyncMsgRecord queueRecord : queueRecords) {
                count++;
                if (count > maxCount) {
                    break; // stop processing if max threshold reached
                }

                log.debug("***** Processing deferred service request for " + queueRecord.getServiceName() + " received at " + queueRecord.getCreationTime() + " *****");

                // Call processing based on the service name

                // - get Request object
                // - call deferred service response processor class
                // - handle Ack
                // - log processed AsyncMsgs record
            }
        }
        else {
            log.debug("***** No queue message records to process were found. *****");
        }

        log.debug("Done: DeferredQueueManagerHelper.forceProcess method - processing deferred queue.");
    }

    /**
     * Set the global threshold gateway property
     */
    private void setGlobalThreshold() {

        try {
            String sGlobalThreshold = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, DEFERRED_QUEUE_GLOBAL_THRESHOLD);
            if ((sGlobalThreshold != null) && (sGlobalThreshold.length() > 0)) {
                iGlobalThreshold = Integer.parseInt(sGlobalThreshold);
            }
        } catch (PropertyAccessException e) {
            String sErrorMessage = "Failed to read and parse " + DEFERRED_QUEUE_GLOBAL_THRESHOLD +
                    " from " + GATEWAY_PROPERTY_FILE + ".properties file - using default " + "" +
                    "value of " + DEFERRED_QUEUE_GLOBAL_THRESHOLD_DEFAULT + " seconds.  Error: " +
                    e.getMessage();
            log.warn(sErrorMessage, e);
        }

    }

    /**
     * Process the deferred patient discovery request
     * @param queueRecord
     * @throws DeferredQueueException
     */
    private void processDeferredPatientDiscovery(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl processImpl = new EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
        processImpl.processPatientDiscoveryAsyncReqQueue(queueRecord.getMessageId());

        log.debug("End: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");
    }

}
