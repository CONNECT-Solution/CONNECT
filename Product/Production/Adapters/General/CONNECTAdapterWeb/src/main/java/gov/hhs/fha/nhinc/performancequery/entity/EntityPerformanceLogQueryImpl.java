/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.performancequery.entity;

import gov.hhs.fha.nhinc.common.entityperformancelogquery.CountDataType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.DetailDataType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.EntityPerformanceLogQueryRequestType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.EntityPerformanceLogQueryResponseType;
import gov.hhs.fha.nhinc.common.entityperformancelogquery.SuccessOrFailType;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class performs the actual work of querying the performance log
 *
 * @author richard.ettema
 */
public class EntityPerformanceLogQueryImpl {

    private static Log log = LogFactory.getLog(EntityPerformanceLogQueryImpl.class);

    /**
     * Return the Performance Log summary based on the request criteria
     *
     * @param entityPerformanceLogQueryRequest
     * @param context
     * @return Performance Response Summary
     */
    public EntityPerformanceLogQueryResponseType findPerformanceData(EntityPerformanceLogQueryRequestType entityPerformanceLogQueryRequest, WebServiceContext context) {
        log.debug("Start - EntityPerformanceLogQueryImpl.findPerformanceData");

        EntityPerformanceLogQueryResponseType response = new EntityPerformanceLogQueryResponseType();
        response.setSuccessOrFail(new SuccessOrFailType());
        response.getSuccessOrFail().setSuccess(false);

        try {
            Calendar beginTime = entityPerformanceLogQueryRequest.getBeginTime().toGregorianCalendar();
            Calendar endTime = entityPerformanceLogQueryRequest.getEndTime().toGregorianCalendar();

            List<CountDataType> countDataList = PerformanceManager.getPerformanceManagerInstance().getPerfrepositoryCountData(beginTime, endTime);
            if (countDataList == null) {
                countDataList = new ArrayList<CountDataType>();
            }
            response.getCountDataList().addAll(countDataList);

            List<DetailDataType> detailDataList = PerformanceManager.getPerformanceManagerInstance().getPerfrepositoryDetailData(beginTime, endTime);
            if (detailDataList == null) {
                detailDataList = new ArrayList<DetailDataType>();
            }
            response.getDetailDataList().addAll(detailDataList);

            // Set success to true is we get this far
            response.getSuccessOrFail().setSuccess(true);
        }
        catch (Exception e) {
            log.error("Exception populating response: ", e);
        }

        log.debug("End - EntityPerformanceLogQueryImpl.findPerformanceData");

        return response;
    }

}
