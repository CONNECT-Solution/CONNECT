/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueRecordType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsDataType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.SuccessOrFailType;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue.EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;

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
     * @param context
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredQueue(DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest, WebServiceContext context) {
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
     * Called via web service interface
     * @param deferredQueueManagerForceProcessRequest
     * @param context
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredRequest(DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest, WebServiceContext context) {
        DeferredQueueManagerForceProcessResponseType oResponse = new DeferredQueueManagerForceProcessResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            if (forceProcessOnRequest(deferredQueueManagerForceProcessRequest.getMessageId())) {
                oResponse.getSuccessOrFail().setSuccess(true);
            }
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to process the Deferred Queue.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     * @param queryDeferredQueueRequest
     * @param context
     * @return queryDeferredQueueResponse
     */
    public QueryDeferredQueueResponseType queryDeferredQueue(QueryDeferredQueueRequestType queryDeferredQueueRequest, WebServiceContext context) {
        QueryDeferredQueueResponseType oResponse = new QueryDeferredQueueResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            oResponse.getDeferredQueueRecord().addAll(queryDeferredQueue(queryDeferredQueueRequest));
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to query the Deferred Queue.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     * @param retrieveDeferredQueueRequest
     * @param context
     * @return retrieveDeferredQueueResponse
     */
    public RetrieveDeferredQueueResponseType retrieveDeferredQueue(RetrieveDeferredQueueRequestType retrieveDeferredQueueRequest, WebServiceContext context) {
        RetrieveDeferredQueueResponseType oResponse = new RetrieveDeferredQueueResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            oResponse.setDeferredQueueRecord(retrieveDeferredQueue(retrieveDeferredQueueRequest));
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to retrieve the Deferred Queue.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     * @param deferredQueueStatisticsRequest
     * @param context
     * @return deferredQueueStatisticsResponse
     */
    public DeferredQueueStatisticsResponseType deferredQueueStatistics(DeferredQueueStatisticsRequestType deferredQueueStatisticsRequest, WebServiceContext context) {
        DeferredQueueStatisticsResponseType oResponse = new DeferredQueueStatisticsResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            oResponse.getDeferredQueueStatisticsData().addAll(queryDeferredQueueStatistics(deferredQueueStatisticsRequest));
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to query the Deferred Queue Statistics.  Error: " + e.getMessage();
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
        AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

        log.debug("***** Check queue message records that are expired and should not be processed *****");
        queueDao.checkExpiration();

        log.debug("***** Retrieve queue message records that are received and not processed *****");
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

                forceProcessOnRequest(queueRecord);
            }
        } else {
            log.debug("***** No queue message records to process were found. *****");
        }

        log.debug("Done: DeferredQueueManagerHelper.forceProcess method - processing deferred queue.");
    }

    /**
     * Force the deferred queue process on a specific request
     * @param messageId
     * @return true - success; false - failure
     * @throws DeferredQueueException
     */
    public boolean forceProcessOnRequest(String messageId) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by messageId.");
        boolean result = false;

        AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

        log.debug("***** Check queue message records that are expired and should not be processed *****");
        queueDao.checkExpiration();

        log.debug("***** Retrieve queue message record to be processed [" + messageId + "] *****");
        List<AsyncMsgRecord> queueRecords = queueDao.queryByMessageId(messageId);

        if (NullChecker.isNotNullish(queueRecords) && queueRecords.size() > 0) {
            if (queueRecords.get(0).getStatus().equals(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK)) {
                result = forceProcessOnRequest(queueRecords.get(0));
            }
            else {
                log.warn("Queue record cannot be processed with status of [" + queueRecords.get(0).getStatus() + "]");
            }
        }
        else {
            log.warn("Queue record not found for message id [" + messageId + "]");
        }

        log.debug("End: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by messageId.");

        return result;
    }

    /**
     * Force the deferred queue process on a specific request
     * @param queueRecord
     * @return true - success; false - failure
     * @throws DeferredQueueException
     */
    public boolean forceProcessOnRequest(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by record.");
        boolean result = false;

        log.debug("***** Processing deferred service request for " + queueRecord.getServiceName() + " received at " + queueRecord.getCreationTime() + " *****");

        // Call processing based on the service name
        if (queueRecord.getServiceName().equals("PatientDiscovery")) {
            MCCIIN000002UV01 ack = processDeferredPatientDiscovery(queueRecord);
            if (ack.getAcceptAckCode().getCode().equals("")) {
                result = true;
            }
        }
        else {
            log.warn("Service Name processing not implemented.");
        }

        log.debug("End: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by record.");

        return result;
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
    private MCCIIN000002UV01 processDeferredPatientDiscovery(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl processImpl = new EntityPatientDiscoveryDeferredReqQueueProcessOrchImpl();
        MCCIIN000002UV01 ack = processImpl.processPatientDiscoveryAsyncReqQueue(queueRecord.getMessageId());

        log.debug("End: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        return ack;
    }

    /**
     * Call deferred queue dao to query for matching records
     * @param queryDeferredQueueRequest
     * @return found list of queue records
     * @throws DeferredQueueException
     */
    private List<DeferredQueueRecordType> queryDeferredQueue(QueryDeferredQueueRequestType queryDeferredQueueRequest) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.queryDeferredQueue method - query deferred messages.");

        List<DeferredQueueRecordType> response = new ArrayList<DeferredQueueRecordType>();

        // TODO: Logic goes here

        log.debug("End: DeferredQueueManagerHelper.queryDeferredQueue method - query deferred messages.");

        return response;
    }

    /**
     * Call deferred queue dao to retrieve message content for one record
     * @param retrieveDeferredQueueRequest
     * @return found list of queue records with message content populated
     * @throws DeferredQueueException
     */
    private DeferredQueueRecordType retrieveDeferredQueue(RetrieveDeferredQueueRequestType retrieveDeferredQueueRequest) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.retrieveDeferredQueue method - retrieve deferred message.");

        DeferredQueueRecordType response = new DeferredQueueRecordType();

        // TODO: Logic goes here

        log.debug("End: DeferredQueueManagerHelper.retrieveDeferredQueue method - retrieve deferred message.");

        return response;
    }

    /**
     * Call deferred queue dao to query for statistics
     * @param deferredQueueStatisticsRequest
     * @return found list of queue statistics records
     * @throws DeferredQueueException
     */
    private List<DeferredQueueStatisticsDataType> queryDeferredQueueStatistics(DeferredQueueStatisticsRequestType deferredQueueStatisticsRequest) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.queryDeferredQueueStatistics method - query deferred statistics.");

        List<DeferredQueueStatisticsDataType> response = new ArrayList<DeferredQueueStatisticsDataType>();

        // TODO: Logic goes here

        log.debug("End: DeferredQueueManagerHelper.queryDeferredQueueStatistics method - query deferred statistics.");

        return response;
    }

}
