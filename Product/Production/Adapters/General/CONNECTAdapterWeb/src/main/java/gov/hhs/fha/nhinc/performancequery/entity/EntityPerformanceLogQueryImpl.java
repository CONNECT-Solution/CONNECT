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

import org.apache.log4j.Logger;

/**
 * This class performs the actual work of querying the performance log
 * 
 * @author richard.ettema
 */
public class EntityPerformanceLogQueryImpl {

    private static final Logger LOG = Logger.getLogger(EntityPerformanceLogQueryImpl.class);

    /**
     * Return the Performance Log summary based on the request criteria
     * 
     * @param entityPerformanceLogQueryRequest
     * @param context
     * @return Performance Response Summary
     */
    public EntityPerformanceLogQueryResponseType findPerformanceData(
            EntityPerformanceLogQueryRequestType entityPerformanceLogQueryRequest, WebServiceContext context) {
        LOG.debug("Start - EntityPerformanceLogQueryImpl.findPerformanceData");

        EntityPerformanceLogQueryResponseType response = new EntityPerformanceLogQueryResponseType();
        response.setSuccessOrFail(new SuccessOrFailType());
        response.getSuccessOrFail().setSuccess(false);

        try {
            Calendar beginTime = entityPerformanceLogQueryRequest.getBeginTime().toGregorianCalendar();
            Calendar endTime = entityPerformanceLogQueryRequest.getEndTime().toGregorianCalendar();

            List<CountDataType> countDataList = PerformanceManager.getPerformanceManagerInstance()
                    .getPerfrepositoryCountData(beginTime, endTime);
            if (countDataList == null) {
                countDataList = new ArrayList<CountDataType>();
            }
            response.getCountDataList().addAll(countDataList);

            List<DetailDataType> detailDataList = PerformanceManager.getPerformanceManagerInstance()
                    .getPerfrepositoryDetailData(beginTime, endTime);
            if (detailDataList == null) {
                detailDataList = new ArrayList<DetailDataType>();
            }
            response.getDetailDataList().addAll(detailDataList);

            // Set success to true is we get this far
            response.getSuccessOrFail().setSuccess(true);
        } catch (Exception e) {
            LOG.error("Exception populating response: ", e);
        }

        LOG.debug("End - EntityPerformanceLogQueryImpl.findPerformanceData");

        return response;
    }

}
