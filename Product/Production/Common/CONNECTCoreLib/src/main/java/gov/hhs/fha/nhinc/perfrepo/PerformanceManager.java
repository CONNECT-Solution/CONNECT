/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.perfrepo;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.hhs.fha.nhinc.common.entityperformancelogquery.CountDataType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.DetailDataType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.dao.PerfrepositoryDao;
import gov.hhs.fha.nhinc.perfrepo.model.Perfrepository;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;

/**
 * PerformanceManager contains methods to consolidate, coordinate and manage dao level functions.
 *
 * @author richard.ettema
 */
public class PerformanceManager {

    private static Log log = LogFactory.getLog(PerformanceManager.class);
    private static PerformanceManager perfManager = new PerformanceManager();
    private static final String PERF_LOG_ENABLED = "performanceLogEnabled";

    /**
     * Constructor
     */
    private PerformanceManager() {
        log.info("PerformanceManager - Initialized");
    }

    /**
     * Singleton instance returned...
     *
     * @return PerformanceManager
     */
    public static PerformanceManager getPerformanceManagerInstance() {
        log.debug("getPerformanceManagerInstance()..");
        return perfManager;
    }

    /**
     * Log new performance repository log record with initial start time
     *
     * @param time
     * @param servicetype
     * @param messagetype
     * @param direction
     * @param communityid
     * @return Long - Generated id from SQL INSERT
     * @deprecated
     */
    @Deprecated
    public Long logPerformanceStart(final Timestamp time, final String servicetype, final String messagetype,
            final String direction, final String communityid) {
        log.debug("PerformanceManager.logPerformanceStart() - Begin");

        Long newId = null;

        if (PerformanceManager.isPerfMonitorEnabled()) {
            newId = createPerformanceRecord(time, servicetype, messagetype, direction, communityid, null, null, null,
                    null, null);
        } else {
            log.info("PerformanceManager.logPerformanceStart() - Performance Monitor is Disabled");
        }

        log.debug("PerformanceManager.logPerformanceStart() - End");

        return newId;
    }

    /**
     * Log new performance repository log record with initial start time
     *
     * @param time
     * @param servicetype
     * @param messagetype
     * @param direction
     * @param communityid
     * @return Long - Generated id from SQL INSERT
     */
    public Long logPerformanceStart(final String servicetype, final String messagetype,
            final String direction, final String communityid) {
        log.debug("PerformanceManager.logPerformanceStart() - Begin");

        Long newId = null;

        if (PerformanceManager.isPerfMonitorEnabled()) {
            newId = createPerformanceRecord(createTimestamp(), servicetype, messagetype, direction, communityid, null, null, null,
                    null, null);
        } else {
            log.info("PerformanceManager.logPerformanceStart() - Performance Monitor is Disabled");
        }

        log.debug("PerformanceManager.logPerformanceStart() - End");

        return newId;
    }

    /**
     * Log new performance repository log record with initial start time
     *
     * @param time
     * @param servicetype
     * @param messagetype
     * @param direction
     * @param communityid
     * @return Long - Generated id from SQL INSERT
     */
    public Long logPerformanceStop(final String servicetype, final String messagetype,
            final String direction, final String communityid) {
        log.debug("PerformanceManager.logPerformanceStart() - Begin");

        Long newId = null;

        if (PerformanceManager.isPerfMonitorEnabled()) {
            newId = createPerformanceRecord(createTimestamp(), servicetype, messagetype, direction, communityid, null, null, null,
                    null, null);
        } else {
            log.info("PerformanceManager.logPerformanceStop() - Performance Monitor is Disabled");
        }

        log.debug("PerformanceManager.logPerformanceStop() - End");

        return newId;
    }

    private static Long createPerformanceRecord(final Timestamp time, final String servicetype, final String messagetype,
            final String direction, final String communityid, final String correlationId, final String errorCode,
            final String messageVersion, final String payLoadSize, final String payLoadType) {
        Long newId = null;
        Perfrepository perfRecord = new Perfrepository();
        perfRecord.setTime(time);
        perfRecord.setServicetype(servicetype);
        perfRecord.setMessagetype(messagetype);
        perfRecord.setDirection(direction);
        perfRecord.setCommunityid(communityid);
        perfRecord.setCorrelationId(correlationId);
        perfRecord.setErrorCode(errorCode);
        perfRecord.setMessageVersion(messageVersion);
        perfRecord.setPayLoadSize(payLoadSize);
        perfRecord.setPayLoadType(payLoadType);

        if (PerfrepositoryDao.getPerfrepositoryDaoInstance().insertPerfrepository(perfRecord)) {
            newId = perfRecord.getId();
            log.info("PerformanceManager.logPerformanceStart() - New Performance Log Id = " + newId);
        } else {
            log.warn("PerformanceManager.logPerformanceStart() - ERROR Inserting New Performance Log Record");
        }
        return newId;
    }

    /**
     * Update stop time and duration for known started performance repository log record
     *
     * @param id
     * @param starttime
     * @param stoptime
     * @param status
     * @deprecated
     * @return Long - Calculated duration in milliseconds
     */
    @Deprecated
    public Long logPerformanceStop(Long id, Timestamp starttime, Timestamp stoptime) {
        log.debug("PerformanceManager.logPerformanceStop() - Begin");

        Long duration = null;

        // Check for valid performance repository id
        if (id != null && id > 0) {
            Perfrepository perfRecord = PerfrepositoryDao.getPerfrepositoryDaoInstance().getPerfrepository(id);

            if (perfRecord != null) {
                // Calculate duration from starttime to stoptime in milliseconds
                if (starttime != null && stoptime != null) {
                    duration = (stoptime.getTime() - starttime.getTime());
                    log.info("PerformanceManager.logPerformanceStop() - Performance Duration = " + duration);
                } else {
                    duration = null;
                    log.warn("PerformanceManager.logPerformanceStop() - ERROR Calculating Performance Duration - starttime and/or stoptime null");
                }

                // perfRecord.setStoptime(stoptime);
                // perfRecord.setDuration(duration);
                // perfRecord.setStatus(status);

                if (!PerfrepositoryDao.getPerfrepositoryDaoInstance().updatePerfrepository(perfRecord)) {
                    duration = null;
                    log.warn("PerformanceManager.logPerformanceStop() - ERROR Updating Performance Log Record");
                }
            }
        } else {
            log.warn("PerformanceManager.logPerformanceStop() - WARN Performance Log Id NULL or Zero(0)");
        }

        log.debug("PerformanceManager.logPerformanceStop() - End");

        return duration;
    }

    /*
     * Return performance count data list for this gateway
     *
     * @param beginTime
     *
     * @param endTime
     *
     * @return countDataList
     */
    public List<CountDataType> getPerfrepositoryCountData(Calendar beginTime, Calendar endTime) {

        log.debug("getPerfrepositoryCountData() method start: beginTime ==" + beginTime + " :::   endTime==" + endTime);

        List<CountDataType> countDataList = PerfrepositoryDao.getPerfrepositoryDaoInstance()
                .getPerfrepositoryCountRange(new Timestamp(beginTime.getTimeInMillis()),
                        new Timestamp(endTime.getTimeInMillis()));

        log.debug("getPerfrepositoryCountData() method end");

        return countDataList;
    }

    /*
     * Return performance detail data list for this gateway
     *
     * @param beginTime
     *
     * @param endTime
     *
     * @return detailDataList
     */
    public List<DetailDataType> getPerfrepositoryDetailData(Calendar beginTime, Calendar endTime) {

        log.debug("getPerfrepositoryDetailData() method start: beginTime ==" + beginTime + " :::   endTime==" + endTime);

        List<DetailDataType> detailDataList = PerfrepositoryDao.getPerfrepositoryDaoInstance()
                .getPerfrepositoryDetailRange(new Timestamp(beginTime.getTimeInMillis()),
                        new Timestamp(endTime.getTimeInMillis()));

        log.debug("getPerfrepositoryDetailData() method end");

        return detailDataList;
    }

    /**
     * Return boolean performance monitor enabled indicator based on gateway property
     *
     * @return
     */
    private static boolean isPerfMonitorEnabled() {
        boolean match = false;
        try {
            // Use CONNECT utility class to access gateway.properties
            String perfEnabled = PropertyAccessor.getInstance().getProperty(NhincConstants.GATEWAY_PROPERTY_FILE,
                    PERF_LOG_ENABLED);
            if (perfEnabled != null && perfEnabled.equalsIgnoreCase("true")) {
                match = true;
            }
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + PERF_LOG_ENABLED + " from property file: "
                    + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return match;
    }

    /**
     * Returns a timestamp, down to the millisecond.
     *
     * @return
     */
    public Timestamp createTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

}
