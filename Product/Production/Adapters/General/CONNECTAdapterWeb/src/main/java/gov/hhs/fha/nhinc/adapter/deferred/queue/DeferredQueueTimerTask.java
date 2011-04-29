/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is responsible for handling the work that is done each time the
 * timer goes off; i.e. processing the outstanding deferred queue request
 * messages.
 *
 * @author richard.ettema
 */
public class DeferredQueueTimerTask {

    private static Log log = null;
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String DEFERRED_QUEUE_SWITCH_PROPERTY = "DeferredQueueProcessActive";

    public DeferredQueueTimerTask() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected void forceDeferredQueueProcess() {
        try {
            DeferredQueueManagerHelper helper = new DeferredQueueManagerHelper();
            helper.forceProcess();
        } catch (DeferredQueueException ex) {
            log.error("DeferredQueueTimerTask DeferredQueueException thrown.", ex);

            StringWriter stackTrace = new StringWriter();
            ex.printStackTrace(new PrintWriter(stackTrace));
            String sValue = stackTrace.toString();
            if (sValue.indexOf("EJBClassLoader") >= 0) {
                DeferredQueueTimer.stopTimer();
            }
        }
    }

    /**
     * This method is called each time the timer thread wakes up.
     */
    public void run() {
        boolean bQueueActive = true;
        try {
            bQueueActive = PropertyAccessor.getPropertyBoolean(GATEWAY_PROPERTY_FILE, DEFERRED_QUEUE_SWITCH_PROPERTY);

            if (bQueueActive) {
                log.debug("Start: DeferredQueueTimerTask.run method - processing queue entries.");

                forceDeferredQueueProcess();

                log.debug("Done: DeferredQueueTimerTask.run method - processing queue entries.");
            } else {
                log.debug("DeferredQueueTimerTask is disabled by the DeferredQueueRefreshActive property.");
            }
        } catch (PropertyAccessException ex) {
            log.error("DeferredQueueTimerTask.run method unable to read DeferredQueueRefreshActive property.", ex);
        }
    }

    /**
     * Main method used to test this class.   This one really should not be run under unit
     * test scenarios because it requires access to the UDDI server.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting test.");

        try {
            DeferredQueueTimerTask oTimerTask = new DeferredQueueTimerTask();
            oTimerTask.run();
        } catch (Exception e) {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("End of test.");
    }
}
