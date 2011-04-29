/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *
 */
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to start a timer which when it wakes up will process any
 * deferred queue request messages.
 *
 * @author richard.ettema
 */
public class DeferredQueueTimer extends Thread {

    private static Log log = LogFactory.getLog(DeferredQueueTimer.class);
    private static DeferredQueueTimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY = "DeferredQueueRefreshDuration";
    private static final int DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT = 600;  //(10 minutes)
    private int m_iDurationSeconds = DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT;

    /**
     * Default constructor
     */
    private DeferredQueueTimer() {
    }

    /**
     * This method is used to crete an instance of the DeferredQueueTimer.
     * There should only be exactly one instance of this running at any time.
     * If it exists, it simply returns the one that exists.  If it does not
     * exist, then it will create it, start the timer, and return a handle to
     * it.
     *
     * @throws DeferredQueueException
     */
    public static void startTimer() throws DeferredQueueException {
        m_bRunnable = true;

        if (m_oTheOneAndOnlyTimer == null) {
            m_oTheOneAndOnlyTimer = new DeferredQueueTimer();
            try {
                m_oTheOneAndOnlyTimer.initialize();
                m_oTheOneAndOnlyTimer.setDaemon(true);
                m_oTheOneAndOnlyTimer.start();
            } catch (Exception e) {
                m_oTheOneAndOnlyTimer = null;
                String sErrorMessage = "Failed to start the Deferred Queue Update Manager timer.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                throw new DeferredQueueException(sErrorMessage, e);
            }

            log.info("DeferredQueueManager timer has just been started now.");
        }
    }

    /**
     * Stop the instance of the DeferredQueueTimer.
     */
    public static void stopTimer() {
        m_bRunnable = false;
    }

    /**
     * This method starts up the timer.
     */
    private void initialize() throws DeferredQueueException {

        try {
            String sDuration = PropertyAccessor.getProperty(GATEWAY_PROPERTY_FILE, DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY);
            if ((sDuration != null) && (sDuration.length() > 0)) {
                m_iDurationSeconds = Integer.parseInt(sDuration);
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to read and parse " + DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY +
                    " from " + GATEWAY_PROPERTY_FILE + ".properties file - using default " + "" +
                    "value of " + DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT + " seconds.  Error: " +
                    e.getMessage();
            log.warn(sErrorMessage, e);
        }

    }

    /**
     * Runs the timer task while the run state is true.
     */
    @Override
    public void run() {
        while (m_bRunnable) {
            DeferredQueueTimerTask oTimerTask = new DeferredQueueTimerTask();
            oTimerTask.run();
            try {
                log.debug("Before reading properties wait status....");
                Thread.sleep(m_iDurationSeconds * 1000);
                log.debug("Now read properties....");
            } catch (InterruptedException ex) {
                log.error("Failed to sleep.", ex);
            }
        }
    }

    /**
     * Main method used to test this class.   This one really should not be run
     * under unit test scenarios because it requires access to the deferred
     * queue.
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting test.");
        log.debug("Log: Starting test.");

        try {
            DeferredQueueTimer.startTimer();
            Thread.sleep(70000);   // 1 minutes 10 seconds
        } catch (Exception e) {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("End of test.");
    }
}
