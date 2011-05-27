/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue.gui.servicefacade;

import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
public class DeferredQueueManagerFacade {

    private static Log log = LogFactory.getLog(DeferredQueueManagerFacade.class);

    protected AsyncMsgRecordDao getAsyncMsgRecordDao() {
        AsyncMsgRecordDao dao = new AsyncMsgRecordDao();
        return dao;
    }

    public List<AsyncMsgRecord> queryQueueRecords(QueryDeferredQueueRequestType queryCriteria) {
        log.debug("Performing query for queue records.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryByCriteria(queryCriteria);

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryForDeferredQueueProcessing() {
        log.debug("Performing query for deferred queue records to process.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryForDeferredQueueProcessing();

        return asyncMsgRecs;
    }

     public List<AsyncMsgRecord> queryForDeferredQueueSelected() {
        log.debug("Performing query for unprocessed queue records.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryForDeferredQueueSelected();

        return asyncMsgRecs;
    }

     public List<AsyncMsgRecord> queryByCreationStartAndStopTime(Date startDate, Date stopDate) {
        log.debug("Performing query for unprocessed queue records.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        QueryDeferredQueueRequestType queryCriteria = new QueryDeferredQueueRequestType();
        queryCriteria.setCreationBeginTime(XMLDateUtil.date2Gregorian(startDate));
        queryCriteria.setCreationEndTime(XMLDateUtil.date2Gregorian(stopDate));
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryByCriteria(queryCriteria);

        return asyncMsgRecs;
    }

}
