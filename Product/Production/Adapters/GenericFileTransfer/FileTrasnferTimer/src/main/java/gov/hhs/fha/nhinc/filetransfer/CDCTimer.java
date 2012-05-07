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
package gov.hhs.fha.nhinc.filetransfer;

import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author dunnek
 * 
 *         Notes:http://forums.java.net/jive/thread.jspa?messageID=265679 Glassfish does not currently send notification
 *         that undeploy has occurred. thread will continue to run.
 */
public class CDCTimer extends Thread {

    private static Log log = LogFactory.getLog(CDCTimer.class);
    private static CDCTimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;
    // private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    private static final String CDC_REFRESH_DURATION_PROPERTY = "CDCRefreshDuration";
    private static final int CDC_REFRESH_DURATION_DEFAULT = 1800; // (30 minutes)
    // private Timer m_oTimer = new Timer(true); // Timer thread - set up as a daemon.
    private int m_iDurationSeconds = CDC_REFRESH_DURATION_DEFAULT;
    private String monitorDir = "";

    /**
     * This method is used to crete an instance of the UDDITimer. There should only be exactly one instance of this
     * running at any time. If it exists, it simply returns the one that exists. If it does not exist, then it will
     * create it, start the timer, and return a handle to it.
     * 
     * @throws UDDIAccessorException
     */
    public static void startTimer() throws CDCTimerException {
        m_bRunnable = true;

        if (m_oTheOneAndOnlyTimer == null) {
            m_oTheOneAndOnlyTimer = new CDCTimer();
            try {
                m_oTheOneAndOnlyTimer.initialize();
                m_oTheOneAndOnlyTimer.setDaemon(true);
                m_oTheOneAndOnlyTimer.start();
            } catch (Exception e) {
                m_oTheOneAndOnlyTimer = null;
                String sErrorMessage = "Failed to start the CDC  timer.  Error: " + e.getMessage();
                log.error(sErrorMessage, e);
                throw new CDCTimerException(sErrorMessage, e);
            }

            log.info("UDDIUpdateManager timer has just been started now.");
        }
    }

    /**
     * 
     */
    public static void stopTimer() {
        m_bRunnable = false;
        m_oTheOneAndOnlyTimer.interrupt();
    }

    /**
     * 
     * @throws gov.hhs.fha.nhinc.cdc.CDCTimerException
     * @throws gov.hhs.fha.nhinc.properties.PropertyAccessException
     */
    private void initialize() throws CDCTimerException, PropertyAccessException {
        String refreshDuration = PropertyAccessor.getInstance().getProperty(ADAPTER_PROPERTY_FILE, CDC_REFRESH_DURATION_PROPERTY);
        if (refreshDuration != null && !refreshDuration.equals("")) {
            m_iDurationSeconds = Integer.parseInt(refreshDuration);
        } else {
            m_iDurationSeconds = 1;
        }
    }

    @Override
    public void run() {
        CDCTimerTask task = new CDCTimerTask();
        task.setMonitorDirectory(this.getMonitorDirectory());

        while (m_bRunnable) {
            task.run();
            try {
                Thread.sleep(m_iDurationSeconds * 1000);
            } catch (InterruptedException ex) {
                log.error("Failed to sleep.", ex);
            }
        }
    }

    /**
     * 
     * @return String
     */
    protected String getMonitorDirectory() {
        String dir = "";
        try {
            dir = PropertyAccessor.getInstance().getProperty(ADAPTER_PROPERTY_FILE, "CDCMonitorDir");
        } catch (Exception ex) {
        }

        return dir;
    }
}
