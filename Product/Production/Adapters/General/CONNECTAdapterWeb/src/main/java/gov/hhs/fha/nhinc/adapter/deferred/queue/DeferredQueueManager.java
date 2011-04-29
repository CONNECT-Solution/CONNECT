/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import javax.jws.WebService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author richard.ettema
 */
@WebService(serviceName = "DeferredQueueManager", portName = "DeferredQueueManagerPort", endpointInterface = "gov.hhs.fha.nhinc.deferredqueuemanager.DeferredQueueManagerPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:deferredqueuemanager", wsdlLocation = "WEB-INF/wsdl/DeferredQueueManager/DeferredQueueManager.wsdl")
public class DeferredQueueManager {

    private static Log log = LogFactory.getLog(DeferredQueueManager.class);

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

    public gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessResponseType forceProcessOnDeferredQueue(gov.hhs.fha.nhinc.common.deferredqueuemanager.DeferredQueueManagerForceProcessRequestType deferredQueueManagerForceProcessRequest) {
        DeferredQueueManagerHelper helper = new DeferredQueueManagerHelper();
        return helper.forceProcessOnDeferredQueue(deferredQueueManagerForceProcessRequest);
    }

}
