/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * Notes:http://forums.java.net/jive/thread.jspa?messageID=265679
 * Glassfish does not currently send notification that undeploy has occurred. 
 * thread will continue to run. 
 */
public class CDCTimer extends Thread {

    private static Log log = LogFactory.getLog(CDCTimer.class);
    private static CDCTimer m_oTheOneAndOnlyTimer = null;
    private static boolean m_bRunnable = false;
    //private static final String GATEWAY_PROPERTY_FILE = "gateway";
    private static final String ADAPTER_PROPERTY_FILE = "adapter";
    private static final String CDC_REFRESH_DURATION_PROPERTY = "CDCRefreshDuration";
    private static final int CDC_REFRESH_DURATION_DEFAULT = 1800;  //(30 minutes)
    // private Timer m_oTimer = new Timer(true);           // Timer thread - set up as a daemon.
    private int m_iDurationSeconds = CDC_REFRESH_DURATION_DEFAULT;
    private String monitorDir = "";

    /**
     * This method is used to crete an instance of the UDDITimer.  There should only be exactly
     * one instance of this running at any time.  If it exists, it simply returns the one that
     * exists.  If it does not exist, then it will create it, start the timer, and return a handle to
     * it.  
     * 
     * @throws UDDIAccessorException
     */
    public static void startTimer()
            throws CDCTimerException {
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
    private void initialize()
            throws CDCTimerException, PropertyAccessException {
        String refreshDuration = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, CDC_REFRESH_DURATION_PROPERTY);
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
            dir = PropertyAccessor.getProperty(ADAPTER_PROPERTY_FILE, "CDCMonitorDir");
        } catch (Exception ex) {
        }

        return dir;
    }
}
