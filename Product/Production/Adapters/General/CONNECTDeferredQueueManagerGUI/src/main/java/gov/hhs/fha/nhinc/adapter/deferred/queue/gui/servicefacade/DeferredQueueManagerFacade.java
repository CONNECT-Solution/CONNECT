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
import gov.hhs.fha.nhinc.util.DeferredGUIConstants;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.webui.jsf.model.Option;

/**
 *
 * @author richard.ettema
 */
public class DeferredQueueManagerFacade implements DeferredGUIConstants {

    private static Log log = LogFactory.getLog(DeferredQueueManagerFacade.class);

    protected AsyncMsgRecordDao getAsyncMsgRecordDao() {
        AsyncMsgRecordDao dao = new AsyncMsgRecordDao();
        return dao;
    }

    public List<AsyncMsgRecord> queryQueueRecords(QueryDeferredQueueRequestType queryCriteria) {
        log.debug("Performing DeferredQueueManagerFacade:: queryQueueRecords API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryByCriteria(queryCriteria);

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryForDeferredQueueProcessing() {
        log.debug("Performing DeferredQueueManagerFacade:: queryForDeferredQueueProcessing API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryForDeferredQueueProcessing();

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryForDeferredQueueSelected() {
        log.debug("Performing DeferredQueueManagerFacade:: queryForDeferredQueueSelected API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryForDeferredQueueSelected();

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryBySearchCriteria(Date startDate, Date stopDate,String status) {
        log.debug("Performing DeferredQueueManagerFacade:: queryByGivenSearchCriteria API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        QueryDeferredQueueRequestType queryCriteria = new QueryDeferredQueueRequestType();
        queryCriteria.setCreationBeginTime(XMLDateUtil.date2Gregorian(startDate));
        queryCriteria.setCreationEndTime(XMLDateUtil.date2Gregorian(stopDate));
        queryCriteria.getStatus().add(status);
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryByCriteria(queryCriteria);

        return asyncMsgRecs;
    }

    public List<Option> queryForDeferredQueueStatuses() {
        log.debug("Performing DeferredQueueManagerFacade:: queryForDeferredQueueStatuses API.");
        List<Option> statuses = loadDeferredQueueStatuses();
        return statuses;
    }

    private List<Option> loadDeferredQueueStatuses() {
        ArrayList statusList = new ArrayList();

        statusList.add(new Option("REQSENTERR",REQSENTERR));
        statusList.add(new Option("REQPROCESS",REQPROCESS));
        statusList.add(new Option("REQSENT",REQSENT));
        statusList.add(new Option("REQSENTACK",REQSENTACK));
        statusList.add(new Option("REQRCVD",REQRCVD));
        statusList.add(new Option("REQRCVDACK",REQRCVDACK));
        statusList.add(new Option("REQRCVDERR",REQRCVDERR));
        statusList.add(new Option("RSPRCVDERR",RSPRCVDERR));
        statusList.add(new Option("RSPRCVD",RSPRCVD));
        statusList.add(new Option("RSPRCVDACK",RSPRCVDACK));
        statusList.add(new Option("RSPSELECT",RSPSELECT));
        statusList.add(new Option("RSPPROCESS",RSPPROCESS));
        statusList.add(new Option("RSPSENT",RSPSENT));
        statusList.add(new Option("RSPSENTACK",RSPSENTACK));
        statusList.add(new Option("RSPSENTERR",RSPSENTERR));
        return statusList;
    }
}
