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
import gov.hhs.fha.nhinc.gateway.adapterdocqueryreqqueueprocess.DocQueryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.adapterdocretrievereqqueueprocess.DocRetrieveDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;
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

        log.debug("***** Retrieve queue message records that were previously selected *****");
        List<AsyncMsgRecord> queueRecords = queueDao.queryForDeferredQueueSelected();

        if (queueRecords == null || queueRecords.size() == 0) {
            log.debug("***** Retrieve queue message records that are received and not processed *****");
            queueRecords = queueDao.queryForDeferredQueueProcessing();
        }

        if (NullChecker.isNotNullish(queueRecords) && queueRecords.size() > 0) {
            int count = 0;
            int maxCount = (queueRecords.size() > iGlobalThreshold ? iGlobalThreshold : queueRecords.size());
            log.debug("***** Found " + queueRecords.size() + " queue message records; will process " + maxCount + " records. *****");

            // Set all statuses of all records to be processed to RSPSELECT
            for (AsyncMsgRecord queueRecord : queueRecords) {
                count++;
                if (count > maxCount) {
                    break; // stop processing if max threshold reached
                }

                queueRecord.setStatus(AsyncMsgRecordDao.QUEUE_STATUS_RSPSELECT);
            }
            queueDao.save(queueRecords);

            // Now process
            count = 0;
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
        List<AsyncMsgRecord> queueRecords = queueDao.queryByMessageIdAndDirection(messageId, AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

        if (NullChecker.isNotNullish(queueRecords) && queueRecords.size() > 0) {
            if (queueRecords.get(0).getStatus().equals(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK)) {
                result = forceProcessOnRequest(queueRecords.get(0));
            } else {
                log.warn("Queue record cannot be processed with status of [" + queueRecords.get(0).getStatus() + "]");
            }
        } else {
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
        if (queueRecord.getServiceName().equals(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME)) {
            PatientDiscoveryDeferredReqQueueProcessResponseType pdResponse = processDeferredPatientDiscovery(queueRecord);

            if (pdResponse != null &&
                    pdResponse.getSuccessOrFail() != null &&
                    pdResponse.getSuccessOrFail().isSuccess()) {
                result = true;
            }

        } else if (queueRecord.getServiceName().equals(NhincConstants.DOC_QUERY_SERVICE_NAME)) {
            DocQueryDeferredReqQueueProcessResponseType qdResponse = processDeferredQueryForDocuments(queueRecord);

            if (qdResponse != null &&
                    qdResponse.getSuccessOrFail() != null &&
                    qdResponse.getSuccessOrFail().isSuccess()) {
                result = true;
            }

        } else if (queueRecord.getServiceName().equals(NhincConstants.DOC_RETRIEVE_SERVICE_NAME)) {
            DocRetrieveDeferredReqQueueProcessResponseType rdResponse = processDeferredRetrieveDocuments(queueRecord);

            if (rdResponse != null &&
                    rdResponse.getSuccessOrFail() != null &&
                    rdResponse.getSuccessOrFail().isSuccess()) {
                result = true;
            }

        } else {
            log.warn("Service Name " + queueRecord.getServiceName() + " processing not implemented.");
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
     * @return Deferred Patient Discovery Response
     * @throws DeferredQueueException
     */
    private PatientDiscoveryDeferredReqQueueProcessResponseType processDeferredPatientDiscovery(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        PatientDiscoveryDeferredReqQueueClient reqClient = new PatientDiscoveryDeferredReqQueueClient();
        PatientDiscoveryDeferredReqQueueProcessResponseType response = reqClient.processPatientDiscoveryDeferredReqQueue(queueRecord.getMessageId());

        log.debug("End: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        return response;
    }

    /**
     * Process the deferred query for documents request
     * @param queueRecord
     * @return Deferred Query For Documents Response
     * @throws DeferredQueueException
     */
    private DocQueryDeferredReqQueueProcessResponseType processDeferredQueryForDocuments(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.processDeferredQueryForDocuments method - processing deferred message.");

        QueryForDocumentsDeferredReqQueueClient reqClient = new QueryForDocumentsDeferredReqQueueClient();
        DocQueryDeferredReqQueueProcessResponseType response = reqClient.processDocQueryDeferredReqQueue(queueRecord.getMessageId());

        log.debug("End: DeferredQueueManagerHelper.processDeferredQueryForDocuments method - processing deferred message.");

        return response;
    }

    /**
     * Process the deferred retrieve documents request
     * @param queueRecord
     * @return Deferred Retrieve Documents Response
     * @throws DeferredQueueException
     */
    private DocRetrieveDeferredReqQueueProcessResponseType processDeferredRetrieveDocuments(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        log.debug("Start: DeferredQueueManagerHelper.processDeferredQueryForDocuments method - processing deferred message.");

        RetrieveDocumentsDeferredReqQueueClient reqClient = new RetrieveDocumentsDeferredReqQueueClient();
        DocRetrieveDeferredReqQueueProcessResponseType response = reqClient.processDocRetrieveDeferredReqQueue(queueRecord.getMessageId());

        log.debug("End: DeferredQueueManagerHelper.processDeferredQueryForDocuments method - processing deferred message.");

        return response;
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

        try {
            AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

            List<AsyncMsgRecord> asyncResponse = queueDao.queryByCriteria(queryDeferredQueueRequest);

            if (asyncResponse != null && asyncResponse.size() > 0) {
                for (AsyncMsgRecord asyncRecord : asyncResponse) {
                    DeferredQueueRecordType queueRecord = new DeferredQueueRecordType();
                    queueRecord.setMessageId(asyncRecord.getMessageId());
                    queueRecord.setCreationTime(XMLDateUtil.date2Gregorian(asyncRecord.getCreationTime()));
                    queueRecord.setResponseTime(XMLDateUtil.date2Gregorian(asyncRecord.getResponseTime()));
                    queueRecord.setDuration(asyncRecord.getDuration());
                    queueRecord.setServiceName(asyncRecord.getServiceName());
                    queueRecord.setDirection(asyncRecord.getDirection());
                    queueRecord.setCommunityId(asyncRecord.getCommunityId());
                    queueRecord.setStatus(asyncRecord.getStatus());
                    queueRecord.setResponseType(asyncRecord.getResponseType());

                    response.add(queueRecord);
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while querying deferred queue: ", e);
            throw new DeferredQueueException(e);
        }

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

        DeferredQueueRecordType response = null;

        try {
            AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

            List<AsyncMsgRecord> asyncResponse = queueDao.queryByMessageIdAndDirection(retrieveDeferredQueueRequest.getMessageId(), AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

            if (asyncResponse != null && asyncResponse.size() > 0) {
                response = new DeferredQueueRecordType();

                response.setMessageId(asyncResponse.get(0).getMessageId());
                response.setCreationTime(XMLDateUtil.date2Gregorian(asyncResponse.get(0).getCreationTime()));
                response.setResponseTime(XMLDateUtil.date2Gregorian(asyncResponse.get(0).getResponseTime()));
                response.setDuration(asyncResponse.get(0).getDuration());
                response.setServiceName(asyncResponse.get(0).getServiceName());
                response.setDirection(asyncResponse.get(0).getDirection());
                response.setCommunityId(asyncResponse.get(0).getCommunityId());
                response.setStatus(asyncResponse.get(0).getStatus());
                response.setResponseType(asyncResponse.get(0).getResponseType());

                if (asyncResponse.get(0).getMsgData() != null && asyncResponse.get(0).getMsgData().length() > 0) {
                    Long msgLength = asyncResponse.get(0).getMsgData().length();
                    response.setMsgData(asyncResponse.get(0).getMsgData().getBytes(1, msgLength.intValue()));
                }

                if (asyncResponse.get(0).getRspData() != null && asyncResponse.get(0).getRspData().length() > 0) {
                    Long rspLength = asyncResponse.get(0).getRspData().length();
                    response.setRspData(asyncResponse.get(0).getRspData().getBytes(1, rspLength.intValue()));
                }

                if (asyncResponse.get(0).getAckData() != null && asyncResponse.get(0).getAckData().length() > 0) {
                    Long ackLength = asyncResponse.get(0).getAckData().length();
                    response.setAckData(asyncResponse.get(0).getAckData().getBytes(1, ackLength.intValue()));
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while retrieving deferred queue: ", e);
            throw new DeferredQueueException(e);
        }

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
