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
package gov.hhs.fha.nhinc.adapter.deferred.queue;

import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is used to start a timer which when it wakes up will process any deferred queue request messages.
 * 
 * @author richard.ettema
 */
public class DeferredQueueTimer extends Thread {

    private static Log log = LogFactory.getLog(DeferredQueueTimer.class);
    private static DeferredQueueTimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;
    private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY = "DeferredQueueRefreshDuration";
    private static final int DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT = 600; // (10 minutes)
    private int m_iDurationSeconds = DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT;

    /**
     * Default constructor
     */
    private DeferredQueueTimer() {
    }

    /**
     * This method is used to crete an instance of the DeferredQueueTimer. There should only be exactly one instance of
     * this running at any time. If it exists, it simply returns the one that exists. If it does not exist, then it will
     * create it, start the timer, and return a handle to it.
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
                String sErrorMessage = "Failed to start the Deferred Queue Update Manager timer.  Error: "
                        + e.getMessage();
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
            String sDuration = PropertyAccessor.getInstance().getProperty(GATEWAY_PROPERTY_FILE,
                    DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY);
            if ((sDuration != null) && (sDuration.length() > 0)) {
                m_iDurationSeconds = Integer.parseInt(sDuration);
            }
        } catch (Exception e) {
            String sErrorMessage = "Failed to read and parse " + DEFERRED_QUEUE_REFRESH_DURATION_PROPERTY + " from "
                    + GATEWAY_PROPERTY_FILE + ".properties file - using default " + "" + "value of "
                    + DEFERRED_QUEUE_REFRESH_DURATION_DEFAULT + " seconds.  Error: " + e.getMessage();
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
     * Main method used to test this class. This one really should not be run under unit test scenarios because it
     * requires access to the deferred queue.
     * 
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Starting test.");
        log.debug("Log: Starting test.");

        try {
            DeferredQueueTimer.startTimer();
            Thread.sleep(70000); // 1 minutes 10 seconds
        } catch (Exception e) {
            System.out.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("End of test.");
    }
}
