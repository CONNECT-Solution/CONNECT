/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueStatisticsResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.QueryDeferredQueueResponseType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueRequestType;
import gov.hhs.fha.nhinc.common.deferredqueuemanager.RetrieveDeferredQueueResponseType;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "DeferredQueueManager", portName = "DeferredQueueManagerPort", endpointInterface = "gov.hhs.fha.nhinc.deferredqueuemanager.DeferredQueueManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:deferredqueuemanager", wsdlLocation = "WEB-INF/wsdl/DeferredQueueManager/DeferredQueueManager.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class DeferredQueueManager {

    private static Log log = LogFactory.getLog(DeferredQueueManager.class);

    @Resource
    private WebServiceContext context;

    /**
     * Default constructor.
     */
    public DeferredQueueManager() {

        try {
            DeferredQueueTimer.startTimer();
        } catch (Exception e) {
            String sErrorMessage = "Failed to start DeferredQueueManager's timer.  Error: " + e.getMessage();
            log.error(sErrorMessage, e);
        }
    }

    /**
     * Force the deferred queue process
     * @param deferredQueueManagerForceProcessRequest
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredQueue(DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        return new DeferredQueueManagerHelper().forceProcessOnDeferredQueue(deferredQueueManagerForceProcessRequest, context);
    }

    /**
     * Force the deferred queue process on a specific request
     * @param deferredQueueManagerForceProcessRequest
     * @return deferredQueueManagerForceProcessResponse
     */
    public DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredRequest(DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        return new DeferredQueueManagerHelper().forceProcessOnDeferredQueue(deferredQueueManagerForceProcessRequest, context);
    }

    /**
     * Query for deferred queue records based on passed criteria
     * @param queryDeferredQueueRequest
     * @return queryDeferredQueueResponse
     */
    public QueryDeferredQueueResponseType queryDeferredQueue(QueryDeferredQueueRequestType queryDeferredQueueRequest) {
        return new DeferredQueueManagerHelper().queryDeferredQueue(queryDeferredQueueRequest, context);
    }

    /**
     * Retrieve a deferred queue record based on passed criteria
     * @param retrieveDeferredQueueRequest
     * @return retrieveDeferredQueueResponse
     */
    public RetrieveDeferredQueueResponseType retrieveDeferredQueue(RetrieveDeferredQueueRequestType retrieveDeferredQueueRequest) {
        return new DeferredQueueManagerHelper().retrieveDeferredQueue(retrieveDeferredQueueRequest, context);
    }

    /**
     * Retrieve deferred queue statistics based on passed criteria
     * @param deferredQueueStatisticsRequest
     * @return deferredQueueStatisticsResponse
     */
    public DeferredQueueStatisticsResponseType deferredQueueStatistics(DeferredQueueStatisticsRequestType deferredQueueStatisticsRequest) {
        return new DeferredQueueManagerHelper().deferredQueueStatistics(deferredQueueStatisticsRequest, context);
    }

}
