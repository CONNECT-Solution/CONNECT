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
package gov.hhs.fha.nhinc.adapter.deferred.queue.gui.servicefacade;

import com.sun.webui.jsf.model.Option;
import gov.hhs.fha.nhinc.asyncmsgs.dao.AsyncMsgRecordDao;
import gov.hhs.fha.nhinc.asyncmsgs.model.AsyncMsgRecord;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.util.DeferredGUIConstants;
import gov.hhs.fha.nhinc.util.format.XMLDateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richard.ettema
 */
public class DeferredQueueManagerFacade implements DeferredGUIConstants {

    private static final Logger LOG = LoggerFactory.getLogger(DeferredQueueManagerFacade.class);

    protected AsyncMsgRecordDao getAsyncMsgRecordDao() {
        AsyncMsgRecordDao dao = new AsyncMsgRecordDao();
        return dao;
    }

    public List<AsyncMsgRecord> queryQueueRecords(QueryDeferredQueueRequestType queryCriteria) {
        LOG.debug("Performing DeferredQueueManagerFacade:: queryQueueRecords API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryByCriteria(queryCriteria);

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryForDeferredQueueProcessing() {
        LOG.debug("Performing DeferredQueueManagerFacade:: queryForDeferredQueueProcessing API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryForDeferredQueueProcessing();

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryForDeferredQueueSelected() {
        LOG.debug("Performing DeferredQueueManagerFacade:: queryForDeferredQueueSelected API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryForDeferredQueueSelected();

        return asyncMsgRecs;
    }

    public List<AsyncMsgRecord> queryBySearchCriteria(Date startDate, Date stopDate, String status) {
        LOG.debug("Performing DeferredQueueManagerFacade:: queryByGivenSearchCriteria API.");

        AsyncMsgRecordDao asyncMsgRecordDao = getAsyncMsgRecordDao();
        QueryDeferredQueueRequestType queryCriteria = new QueryDeferredQueueRequestType();
        queryCriteria.setCreationBeginTime(XMLDateUtil.date2Gregorian(startDate));
        queryCriteria.setCreationEndTime(XMLDateUtil.date2Gregorian(stopDate));
        queryCriteria.getStatus().add(status);
        List<AsyncMsgRecord> asyncMsgRecs = asyncMsgRecordDao.queryByCriteria(queryCriteria);

        return asyncMsgRecs;
    }

    public List<Option> queryForDeferredQueueStatuses() {
        LOG.debug("Performing DeferredQueueManagerFacade:: queryForDeferredQueueStatuses API.");
        List<Option> statuses = loadDeferredQueueStatuses();
        return statuses;
    }

    private List<Option> loadDeferredQueueStatuses() {
        ArrayList statusList = new ArrayList();

        statusList.add(new Option("REQSENTERR", REQSENTERR));
        statusList.add(new Option("REQPROCESS", REQPROCESS));
        statusList.add(new Option("REQSENT", REQSENT));
        statusList.add(new Option("REQSENTACK", REQSENTACK));
        statusList.add(new Option("REQRCVD", REQRCVD));
        statusList.add(new Option("REQRCVDACK", REQRCVDACK));
        statusList.add(new Option("REQRCVDERR", REQRCVDERR));
        statusList.add(new Option("RSPRCVDERR", RSPRCVDERR));
        statusList.add(new Option("RSPRCVD", RSPRCVD));
        statusList.add(new Option("RSPRCVDACK", RSPRCVDACK));
        statusList.add(new Option("RSPSELECT", RSPSELECT));
        statusList.add(new Option("RSPPROCESS", RSPPROCESS));
        statusList.add(new Option("RSPSENT", RSPSENT));
        statusList.add(new Option("RSPSENTACK", RSPSENTACK));
        statusList.add(new Option("RSPSENTERR", RSPSENTERR));
        return statusList;
    }
}
