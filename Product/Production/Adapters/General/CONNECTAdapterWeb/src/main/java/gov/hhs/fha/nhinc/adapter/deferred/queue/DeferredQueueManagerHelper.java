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
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueRecordType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsDataType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.SuccessOrFailType;
import gov.hhs.fha.nhinc.gateway.adapterpatientdiscoveryreqqueueprocess.PatientDiscoveryDeferredReqQueueProcessResponseType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for the web service.
 *
 * @author richard.ettema
 */
public class DeferredQueueManagerHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DeferredQueueManagerHelper.class);
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String DEFERRED_QUEUE_GLOBAL_THRESHOLD = "DeferredQueueGlobalThreshold";
    private static final int DEFERRED_QUEUE_GLOBAL_THRESHOLD_DEFAULT = 100;
    private int iGlobalThreshold = DEFERRED_QUEUE_GLOBAL_THRESHOLD_DEFAULT;

    /**
     * Called via web service interface
     *
     * @param deferredQueueManagerForceProcessRequest
     * @param context
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredQueue(
        DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest,
        WebServiceContext context) {
        DeferredQueueManagerForceProcessResponseType oResponse = new DeferredQueueManagerForceProcessResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            forceProcess();
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to process the Deferred Queue.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     *
     * @param deferredQueueManagerForceProcessRequest
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredRequest(
        DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        DeferredQueueManagerForceProcessResponseType oResponse = new DeferredQueueManagerForceProcessResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            if (forceProcessOnRequest(deferredQueueManagerForceProcessRequest.getMessageId())) {
                oResponse.getSuccessOrFail().setSuccess(true);
            }
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to process the Deferred Queue.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     *
     * @param queryDeferredQueueRequest
     * @return queryDeferredQueueResponse
     */
    public QueryDeferredQueueResponseType queryDeferredQueue(
        QueryDeferredQueueRequestType queryDeferredQueueRequest) {
        QueryDeferredQueueResponseType oResponse = new QueryDeferredQueueResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            oResponse.getDeferredQueueRecord().addAll(queryDeferredQueueFrom(queryDeferredQueueRequest));
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to query the Deferred Queue.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     *
     * @param retrieveDeferredQueueRequest
     * @return retrieveDeferredQueueResponse
     */
    public RetrieveDeferredQueueResponseType retrieveDeferredQueue(
        RetrieveDeferredQueueRequestType retrieveDeferredQueueRequest) {
        RetrieveDeferredQueueResponseType oResponse = new RetrieveDeferredQueueResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            oResponse.setDeferredQueueRecord(retrieveDeferredQueueFrom(retrieveDeferredQueueRequest));
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to retrieve the Deferred Queue.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Called via web service interface
     *
     * @param deferredQueueStatisticsRequest
     * @return deferredQueueStatisticsResponse
     */
    public DeferredQueueStatisticsResponseType deferredQueueStatistics(
        DeferredQueueStatisticsRequestType deferredQueueStatisticsRequest) {
        DeferredQueueStatisticsResponseType oResponse = new DeferredQueueStatisticsResponseType();
        oResponse.setSuccessOrFail(new SuccessOrFailType());
        oResponse.getSuccessOrFail().setSuccess(false);

        try {
            oResponse.getDeferredQueueStatisticsData()
            .addAll(queryDeferredQueueStatistics(deferredQueueStatisticsRequest));
            oResponse.getSuccessOrFail().setSuccess(true);
        } catch (DeferredQueueException e) {
            String sErrorMessage = "Failed to query the Deferred Queue Statistics.  Error: " + e.getMessage();
            LOG.error(sErrorMessage, e);
        }

        return oResponse;
    }

    /**
     * Force the deferred queue process
     *
     * @throws DeferredQueueException
     */
    public void forceProcess() throws DeferredQueueException {
        LOG.debug("Start: DeferredQueueManagerHelper.forceProcess method - processing deferred queue.");

        setGlobalThreshold();

        // Retrieve AsyncMsgs records that are received and not processed
        AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

        LOG.debug("***** Check queue message records that are expired and should not be processed *****");
        queueDao.checkExpiration();

        LOG.debug("***** Retrieve queue message records that were previously selected *****");
        List<AsyncMsgRecord> queueRecords = queueDao.queryForDeferredQueueSelected();

        if (CollectionUtils.isEmpty(queueRecords)) {
            LOG.debug("***** Retrieve queue message records that are received and not processed *****");
            queueRecords = queueDao.queryForDeferredQueueProcessing();
        }

        if (CollectionUtils.isNotEmpty(queueRecords)) {
            int count = 0;
            int maxCount = queueRecords.size() > iGlobalThreshold ? iGlobalThreshold : queueRecords.size();
            LOG.debug("***** Found " + queueRecords.size() + " queue message records; will process " + maxCount
                + " records. *****");

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
            LOG.debug("***** No queue message records to process were found. *****");
        }

        LOG.debug("Done: DeferredQueueManagerHelper.forceProcess method - processing deferred queue.");
    }

    /**
     * Force the deferred queue process on a specific request
     *
     * @param messageId
     * @return true - success; false - failure
     * @throws DeferredQueueException
     */
    public boolean forceProcessOnRequest(String messageId) throws DeferredQueueException {
        LOG.debug(
            "Start: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by messageId.");
        boolean result = false;

        AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

        LOG.debug("***** Check queue message records that are expired and should not be processed *****");
        queueDao.checkExpiration();

        LOG.debug("***** Retrieve queue message record to be processed [" + messageId + "] *****");
        List<AsyncMsgRecord> queueRecords = queueDao.queryByMessageIdAndDirection(messageId,
            AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

        if (CollectionUtils.isNotEmpty(queueRecords)) {
            if (queueRecords.get(0).getStatus().equals(AsyncMsgRecordDao.QUEUE_STATUS_REQRCVDACK)) {
                result = forceProcessOnRequest(queueRecords.get(0));
            } else {
                LOG.warn("Queue record cannot be processed..");
            }
        } else {
            LOG.warn("Queue record not found for message id [" + messageId + "]");
        }

        LOG.debug(
            "End: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by messageId.");

        return result;
    }

    /**
     * Force the deferred queue process on a specific request
     *
     * @param queueRecord
     * @return true - success; false - failure
     * @throws DeferredQueueException
     */
    public boolean forceProcessOnRequest(AsyncMsgRecord queueRecord) throws DeferredQueueException {
        LOG.debug(
            "Start: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by record.");
        boolean result = false;

        // Call processing based on the service name
        if (queueRecord.getServiceName().equals(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME)) {
            PatientDiscoveryDeferredReqQueueProcessResponseType pdResponse = processDeferredPatientDiscovery(
                queueRecord);

            if (pdResponse != null && pdResponse.getSuccessOrFail() != null
                && pdResponse.getSuccessOrFail().isSuccess()) {
                result = true;
            }

        } else {
            LOG.warn("Service processing not implemented, Please check the Service Name.");
        }

        LOG.debug(
            "End: DeferredQueueManagerHelper.forceProcessOnRequest method - processing deferred request by record.");

        return result;
    }

    /**
     * Set the global threshold gateway property
     */
    private void setGlobalThreshold() {

        try {
            String sGlobalThreshold = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE,
                DEFERRED_QUEUE_GLOBAL_THRESHOLD);
            if (sGlobalThreshold != null && sGlobalThreshold.length() > 0) {
                iGlobalThreshold = Integer.parseInt(sGlobalThreshold);
            }
        } catch (PropertyAccessException e) {
            String sErrorMessage = "Failed to read and parse " + DEFERRED_QUEUE_GLOBAL_THRESHOLD + " from "
                + GATEWAY_PROPERTY_FILE + ".properties file - using default " + "" + "value of "
                + DEFERRED_QUEUE_GLOBAL_THRESHOLD_DEFAULT + " seconds.  Error: " + e.getMessage();
            LOG.warn(sErrorMessage, e);
        }

    }

    /**
     * Process the deferred patient discovery request
     *
     * @param queueRecord
     * @return Deferred Patient Discovery Response
     */
    private static PatientDiscoveryDeferredReqQueueProcessResponseType processDeferredPatientDiscovery(
        AsyncMsgRecord queueRecord) {
        LOG.debug(
            "Start: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        PatientDiscoveryDeferredReqQueueClient reqClient = new PatientDiscoveryDeferredReqQueueClient();
        PatientDiscoveryDeferredReqQueueProcessResponseType response = reqClient
            .processPatientDiscoveryDeferredReqQueue(queueRecord.getMessageId());

        LOG.debug(
            "End: DeferredQueueManagerHelper.processDeferredPatientDiscovery method - processing deferred message.");

        return response;
    }

    /**
     * Call deferred queue dao to query for matching records
     *
     * @param queryDeferredQueueRequest
     * @return found list of queue records
     * @throws DeferredQueueException
     */
    private static List<DeferredQueueRecordType> queryDeferredQueueFrom(
        QueryDeferredQueueRequestType queryDeferredQueueRequest)
            throws DeferredQueueException {
        LOG.debug("Start: DeferredQueueManagerHelper.queryDeferredQueue method - query deferred messages.");

        List<DeferredQueueRecordType> response = new ArrayList<>();

        try {
            AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

            List<AsyncMsgRecord> asyncResponse = queueDao.queryByCriteria(queryDeferredQueueRequest);

            if (CollectionUtils.isNotEmpty(asyncResponse)) {
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
            LOG.error("Exception occurred while querying deferred queue: ", e);
            throw new DeferredQueueException(e);
        }

        LOG.debug("End: DeferredQueueManagerHelper.queryDeferredQueue method - query deferred messages.");

        return response;
    }

    /**
     * Call deferred queue dao to retrieve message content for one record
     *
     * @param retrieveDeferredQueueRequest
     * @return found list of queue records with message content populated
     * @throws DeferredQueueException
     */
    private static DeferredQueueRecordType retrieveDeferredQueueFrom(
        RetrieveDeferredQueueRequestType retrieveDeferredQueueRequest)
            throws DeferredQueueException {
        LOG.debug("Start: DeferredQueueManagerHelper.retrieveDeferredQueue method - retrieve deferred message.");

        DeferredQueueRecordType response = null;

        try {
            AsyncMsgRecordDao queueDao = new AsyncMsgRecordDao();

            List<AsyncMsgRecord> asyncResponse = queueDao.queryByMessageIdAndDirection(
                retrieveDeferredQueueRequest.getMessageId(), AsyncMsgRecordDao.QUEUE_DIRECTION_INBOUND);

            if (CollectionUtils.isNotEmpty(asyncResponse)) {
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
            LOG.error("Exception occurred while retrieving deferred queue: ", e);
            throw new DeferredQueueException(e);
        }

        LOG.debug("End: DeferredQueueManagerHelper.retrieveDeferredQueue method - retrieve deferred message.");

        return response;
    }

    /**
     * Call deferred queue dao to query for statistics
     *
     * @param deferredQueueStatisticsRequest
     * @return found list of queue statistics records
     * @throws DeferredQueueException
     */
    private static List<DeferredQueueStatisticsDataType> queryDeferredQueueStatistics(
        DeferredQueueStatisticsRequestType deferredQueueStatisticsRequest) throws DeferredQueueException {
        LOG.debug("Start: DeferredQueueManagerHelper.queryDeferredQueueStatistics method - query deferred statistics.");

        List<DeferredQueueStatisticsDataType> response = new ArrayList<>();

        // TODO: Logic goes here
        LOG.debug("End: DeferredQueueManagerHelper.queryDeferredQueueStatistics method - query deferred statistics.");

        return response;
    }
}
