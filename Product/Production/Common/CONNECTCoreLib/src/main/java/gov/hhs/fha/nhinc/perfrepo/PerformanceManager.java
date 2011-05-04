/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.perfrepo;

import gov.hhs.fha.nhinc.common.entityperformancelogquery.CountDataType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.DetailDataType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.perfrepo.dao.PerfrepositoryDao;
import gov.hhs.fha.nhinc.perfrepo.model.Perfrepository;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PerformanceManager contains methods to consolidate, coordinate and manage
 * dao level functions.
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
     * @return PerformanceManager
     */
    public static PerformanceManager getPerformanceManagerInstance() {
        log.debug("getPerformanceManagerInstance()..");
        return perfManager;
    }

    /*
     * Methods for managing the add and update of perfrepository records
     */

    /**
     * Log new performance repository log record with initial start time
     * @param starttime
     * @param servicetype
     * @param messagetype
     * @param direction
     * @param communityid
     * @return Long - Generated id from SQL INSERT
     */
    public Long logPerformanceStart(Timestamp starttime, String servicetype, String messagetype, String direction, String communityid) {
        log.debug("PerformanceManager.logPerformanceStart() - Begin");

        Long newId = null;

        if (IsPerfMonitorEnabled()) {
            Perfrepository perfRecord = new Perfrepository();
            perfRecord.setStarttime(starttime);
            perfRecord.setServicetype(servicetype);
            perfRecord.setMessagetype(messagetype);
            perfRecord.setDirection(direction);
            perfRecord.setCommunityid(communityid);

            if (PerfrepositoryDao.getPerfrepositoryDaoInstance().insertPerfrepository(perfRecord)) {
                newId = perfRecord.getId();
                log.info("PerformanceManager.logPerformanceStart() - New Performance Log Id = " + newId);
            }
            else {
                log.warn("PerformanceManager.logPerformanceStart() - ERROR Inserting New Performance Log Record");
            }
        }
        else {
            log.info("PerformanceManager.logPerformanceStart() - Performance Monitor is Disabled");
        }

        log.debug("PerformanceManager.logPerformanceStart() - End");

        return newId;
    }

    /**
     * Update stop time and duration for known started performance repository log record
     * @param id
     * @param starttime
     * @param stoptime
     * @param status
     * @return Long - Calculated duration in milliseconds
     */
    public Long logPerformanceStop(Long id, Timestamp starttime, Timestamp stoptime) {
        log.debug("PerformanceManager.logPerformanceStop() - Begin");

        Long duration = null;
        Integer status = 0;

        // Check for valid performance repository id
        if (id != null && id > 0) {
            Perfrepository perfRecord = PerfrepositoryDao.getPerfrepositoryDaoInstance().getPerfrepository(id);

            if (perfRecord != null) {
                // Calculate duration from starttime to stoptime in milliseconds
                if (starttime != null && stoptime != null) {
                    duration = (stoptime.getTime() - starttime.getTime());
                    log.info("PerformanceManager.logPerformanceStop() - Performance Duration = " + duration);
                }
                else {
                    status = -1;
                    duration = null;
                    log.warn("PerformanceManager.logPerformanceStop() - ERROR Calculating Performance Duration - starttime and/or stoptime null");
                }

                perfRecord.setStoptime(stoptime);
                perfRecord.setDuration(duration);
                perfRecord.setStatus(status);

                if (!PerfrepositoryDao.getPerfrepositoryDaoInstance().updatePerfrepository(perfRecord)) {
                    status = -1;
                    duration = null;
                    log.warn("PerformanceManager.logPerformanceStop() - ERROR Updating Performance Log Record");
                }
            }
        }
        else {
            log.warn("PerformanceManager.logPerformanceStop() - WARN Performance Log Id NULL or Zero(0)");
        }

        log.debug("PerformanceManager.logPerformanceStop() - End");

        return duration;
    }

    /*
     * Return performance count data list for this gateway
     * @param beginTime
     * @param endTime
     * @return countDataList
     */
    public List<CountDataType> getPerfrepositoryCountData(Calendar beginTime, Calendar endTime) {

        log.debug("getPerfrepositoryCountData() method start: beginTime ==" + beginTime + " :::   endTime==" + endTime);

        List<CountDataType> countDataList = PerfrepositoryDao.getPerfrepositoryDaoInstance().getPerfrepositoryCountRange(new Timestamp(beginTime.getTimeInMillis()), new Timestamp(endTime.getTimeInMillis()));

        log.debug("getPerfrepositoryCountData() method end");

        return countDataList;
    }

    /*
     * Return performance detail data list for this gateway
     * @param beginTime
     * @param endTime
     * @return detailDataList
     */
    public List<DetailDataType> getPerfrepositoryDetailData(Calendar beginTime, Calendar endTime) {

        log.debug("getPerfrepositoryDetailData() method start: beginTime ==" + beginTime + " :::   endTime==" + endTime);

        List<DetailDataType> detailDataList = PerfrepositoryDao.getPerfrepositoryDaoInstance().getPerfrepositoryDetailRange(new Timestamp(beginTime.getTimeInMillis()), new Timestamp(endTime.getTimeInMillis()));

        log.debug("getPerfrepositoryDetailData() method end");

        return detailDataList;
    }

    /**
     * Return boolean performance monitor enabled indicator based on gateway property
     * @return
     */
    private static boolean IsPerfMonitorEnabled() {
        boolean match = false;
        try {
            // Use CONNECT utility class to access gateway.properties
            String perfEnabled = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, PERF_LOG_ENABLED);
            if (perfEnabled != null && perfEnabled.equalsIgnoreCase("true")) {
                match = true;
            }
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + PERF_LOG_ENABLED + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return match;
    }

}
